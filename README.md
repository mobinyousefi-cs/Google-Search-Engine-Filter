# Google Search Engine Filter

A professional, modular Java application that interacts with **Google Custom Search Engine (CSE)** and applies advanced filters to the returned results. This project is designed in a clean, extensible architecture suitable for academic, enterprise, and personal use.

Author: **Mobin Yousefi**  
GitHub: https://github.com/mobinyousefi-cs

---

## Overview
This application provides a **CLI interface** that lets users:
- Run Google search queries via Google CSE REST API.
- Apply flexible filters such as:
  - Date ranges (from/to)
  - Domain whitelist / blacklist
  - MIME types
  - Languages
  - Safe content flag
  - Maximum number of results

The result is a controlled and precise search workflow ideal for research, data collection, and automated systems.

The architecture separates concerns across packages:
- `model` → Data models (SearchResult, FilterCriteria)
- `service` → Search client + search filter engine
- `ui` → CLI interface
- `util` → Configuration utilities
- `exception` → Custom exception types

---

## Features
- Fully modular OOP design
- Robust HTTP interaction via Java `HttpClient`
- JSON parsing with Jackson
- Efficient in-memory filtering
- Clear separation between search logic and presentation layer
- Production-ready structure using **Maven**

---

## Project Structure
```
google-search-engine-filter/
├─ pom.xml
├─ LICENSE
├─ src/
│  └─ main/
│     ├─ java/
│     │  └─ com.mobinyousefi.googlesearchfilter/
│     │     ├─ GoogleSearchFilterApp.java
│     │     ├─ exception/
│     │     │  └─ SearchException.java
│     │     ├─ model/
│     │     │  ├─ FilterCriteria.java
│     │     │  └─ SearchResult.java
│     │     ├─ service/
│     │     │  ├─ SearchClient.java
│     │     │  ├─ GoogleSearchClient.java
│     │     │  └─ SearchFilter.java
│     │     ├─ ui/
│     │     │  └─ ConsoleUI.java
│     │     └─ util/
│     │        └─ Config.java
│     └─ resources/
│        └─ config.properties
```

---

## Requirements
- Java 17+
- Maven 3.8+
- A valid Google Custom Search Engine (CSE):
  - `google.apiKey`
  - `google.searchEngineId`

---

## Installation & Setup

### 1. Clone the repository
```bash
git clone https://github.com/mobinyousefi-cs/google-search-engine-filter.git
cd google-search-engine-filter
```

### 2. Set up configuration
Edit the file:
```
src/main/resources/config.properties
```
And enter:
```properties
google.apiKey=YOUR_API_KEY
google.searchEngineId=YOUR_SEARCH_ENGINE_ID
```
**Never commit real API keys to GitHub.**

### 3. Build the project
```bash
mvn clean package
```

### 4. Run the application
```bash
java -jar target/google-search-engine-filter-1.0.0-SNAPSHOT.jar
```

---

## How to Use (CLI)
The program will prompt you:
- Enter your search query
- Choose filtering options:
  - Max results
  - Date filtering (ISO format)
  - Domain restrictions
  - MIME type filters
  - Language filters
  - Safe search flag

Example:
```
> Enter search query: deep learning research papers
> Max results [20]: 10
> Filter by from-date (ISO): 2023-01-01T00:00:00Z
> Domain whitelist: arxiv.org,springer.com
> MIME types: application/pdf
> Restrict languages: en
> Safe results only? [Y/n]: Y
```
The filtered search results are then displayed cleanly.

---

## Extending the Project
You can easily add:
- New search providers (Bing, DuckDuckGo, local index)
- Web UI or desktop GUI
- Database storage for search histories
- Additional filter strategies (regex URL filters, keyword filters, ranking metrics)

---

## License
This project is licensed under the **MIT License**.

---

If you want, I can generate:
- A professional banner for the README
- A GitHub Shields section (Java version, license, build status, etc.)
- A more advanced “Usage Examples” section
- A developer guide (package-by-package explanation)
