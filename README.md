# Java Inverted-Index Search Engine

A high-performance desktop search utility built in Java. This project implements an **Inverted Index** data structure to allow for instantaneous document retrieval across a local corpus, bypassing the need for linear $O(n)$ scanning.

## 🛠 Technical Architecture

Unlike a basic grep-style search, this engine uses a two-phase process:
1. **Indexing Phase:** The engine parses the corpus, tokenizes text, and maps every unique word to a list of its locations (Postings List).
2. **Query Phase:** Searches are performed in $O(1)$ time relative to the number of documents, as the engine only looks up the pre-indexed keys.

### Core Features
* **Inverted Index Logic:** Efficient memory mapping of terms to document references.
* **High-Contrast GUI:** A custom Swing interface utilizing a professional Orange/Black theme for high legibility.
* **Tokenization Pipeline:** Basic NLP processing to normalize terms (case-insensitivity and punctuation removal).
* **Instant Results:** Real-time filtering as you type.

## 🚀 Getting Started

### Prerequisites
* **JDK 17** or higher.
* Any standard IDE (IntelliJ, Eclipse, or VS Code).
