import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    // Backend 

    static ArrayList<String> loadFromFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null)
                lines.add(line.trim().toLowerCase());
        } catch (IOException e) {
            System.out.println("Could not load file: " + filename);
        }
        return lines;
    }

    static String readFile(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null)
                content.append(line.toLowerCase()).append(" ");
        } catch (IOException e) {
            System.out.println("Could not read file: " + filename);
        }
        return content.toString().trim();
    }

    public static void main(String[] args) {

        // Build index first
        ArrayList<String> stopWords = loadFromFile("stopwords.txt");
        HashMap<String, HashMap<String, Integer>> index = new HashMap<>();

        File corpus = new File("Corpus");
        File[] files = corpus.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null) {
            JOptionPane.showMessageDialog(null, "Corpus folder not found or empty.");
            return;
        }

        for (File file : files) {
            String content = readFile(file.getPath());
            for (String word : content.split(" ")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                if (word.isEmpty() || stopWords.contains(word)) continue;
                index.putIfAbsent(word, new HashMap<>());
                HashMap<String, Integer> fileCounts = index.get(word);
                fileCounts.put(file.getName(), fileCounts.getOrDefault(file.getName(), 0) + 1);
            }
        }

        int totalFiles = files.length;
             // ─── GUI ───
        // ── Swing UI ──────────────────────────────────────────────────────
      // --- Colors (Orange & Black Theme) ---
Color BG      = new Color(10, 10, 10);    // Deep Black
Color SURFACE = new Color(30, 30, 30);    // Dark Grey for cards/fields
Color ACCENT  = new Color(255, 153, 0);   // The Orange
Color ACCENT2 = new Color(255, 122, 0);   // Darker Orange for hover/borders
Color TEXT    = new Color(255, 255, 255); // Pure White for text
Color MUTED   = new Color(255, 153, 0); // The Orange
Color SUCCESS = new Color(255, 153, 0);  // The Orange
Color DANGER  = new Color(248, 113, 113); // Red

        Font MONO   = new Font("Monospaced",  Font.PLAIN, 13);
        Font TITLE  = new Font("SansSerif",   Font.BOLD,  22);
        Font LABEL  = new Font("SansSerif",   Font.BOLD,  12);
        Font BODY   = new Font("SansSerif",   Font.PLAIN, 13);

        // Frame
        JFrame frame = new JFrame("Local Search Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 560);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Root panel
        JPanel root = new JPanel();
        root.setLayout(new BorderLayout(0, 0));
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(32, 36, 28, 36));

        // ── Header ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JLabel titleLabel = new JLabel("Local Search Engine");
        titleLabel.setFont(TITLE);
        titleLabel.setForeground(TEXT);

        JLabel statusLabel = new JLabel("● " + totalFiles + " documents indexed");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(SUCCESS);

        header.add(titleLabel,  BorderLayout.WEST);
        header.add(statusLabel, BorderLayout.EAST);

        // ── Search bar ────────────────────────────────────────────────────
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(BG);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JTextField queryField = new JTextField(20);
        queryField.setFont(new Font("Arial", Font.PLAIN, 18));
        queryField.setBackground(Color.black);
        queryField.setOpaque(true); 
        queryField.setForeground(Color.white); 
        queryField.setCaretColor(Color.orange);  
        queryField.repaint();
        queryField.setOpaque(true);
        queryField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(55, 65, 81), 1, true),
            BorderFactory.createEmptyBorder(0, 3, 0, 3)
        ));

        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(LABEL);
        searchBtn.setBackground(ACCENT2);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(100, 42));

        // Hover effect on button
        searchBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                searchBtn.setBackground(new Color(99, 102, 241));
            }
            public void mouseExited(MouseEvent e) {
                searchBtn.setBackground(ACCENT2);
            }
        });

        searchPanel.add(queryField, BorderLayout.CENTER);
        searchPanel.add(searchBtn,  BorderLayout.EAST);

        // ── Results label ─────────────────────────────────────────────────
        JLabel resultsLabel = new JLabel("Results will appear here");
        resultsLabel.setFont(LABEL);
        resultsLabel.setForeground(MUTED);
        resultsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // ── Results area ──────────────────────────────────────────────────
        JTextArea resultsArea = new JTextArea();
        resultsArea.setFont(MONO);
        resultsArea.setBackground(SURFACE);
        resultsArea.setForeground(TEXT);
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        resultsArea.setCaretColor(ACCENT);

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(new LineBorder(new Color(55, 65, 81), 1, true));
        scrollPane.getViewport().setBackground(SURFACE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // ── Footer ────────────────────────────────────────────────────────
        JLabel footer = new JLabel("Press Enter or click Search · Type 'exit' to quit");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setForeground(MUTED);
        footer.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        footer.setHorizontalAlignment(SwingConstants.CENTER);

        // ── Search logic (shared by button + Enter key) ───────────────────
        Runnable doSearch = () -> {
            String query = queryField.getText().toLowerCase().trim();
            if (query.isEmpty()) return;

            if (query.equals("exit")) {
                System.exit(0);
            }

            if (stopWords.contains(query)) {
                resultsLabel.setForeground(DANGER);
                resultsLabel.setText("Common word — ignored");
                resultsArea.setText("\"" + query + "\" is a stop word and was not indexed.\n"
                    + "Try a more specific word.");

            } else if (index.containsKey(query)) {
                // Sort by frequency descending
                java.util.List<Map.Entry<String, Integer>> ranked =
                    new ArrayList<>(index.get(query).entrySet());
                ranked.sort((a, b) -> b.getValue() - a.getValue());

                resultsLabel.setForeground(SUCCESS);
                resultsLabel.setText(ranked.size() + " result(s) for  \"" + query + "\"");

                StringBuilder sb = new StringBuilder();
                int rank = 1;
                for (Map.Entry<String, Integer> entry : ranked) {
                    sb.append(rank++)
                      .append(".  ")
                      .append(entry.getKey())
                      .append("   —   ")
                      .append(entry.getValue())
                      .append(entry.getValue() == 1 ? " occurrence" : " occurrences")
                      .append("\n");
                }
                resultsArea.setText(sb.toString());

            } else {
                resultsLabel.setForeground(DANGER);
                resultsLabel.setText("No results for  \"" + query + "\"");
                resultsArea.setText("No documents contain this word.\n"
                    + "Check spelling or try a different term.");
            }

            queryField.selectAll();
        };

        // Wire up button and Enter key
        searchBtn.addActionListener(e -> doSearch.run());
        queryField.addActionListener(e -> doSearch.run());

        // ── Assemble ──────────────────────────────────────────────────────
        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setBackground(BG);
        centerPanel.add(resultsLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane,   BorderLayout.CENTER);

        root.add(header,      BorderLayout.NORTH);
        root.add(searchPanel, BorderLayout.CENTER);
        root.add(centerPanel, BorderLayout.SOUTH);

        // Fix height of centerPanel (scroll area)
        centerPanel.setPreferredSize(new Dimension(648, 340));

        frame.add(root);
        frame.add(footer, BorderLayout.SOUTH);

        frame.setVisible(true);
        queryField.requestFocusInWindow();
    }
}