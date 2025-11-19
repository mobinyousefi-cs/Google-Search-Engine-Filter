/*
 * =====================================================================================================================
 * Project: Google Search Engine Filter
 * File: ConsoleUI.java
 * Author: Mobin Yousefi (GitHub: https://github.com/mobinyousefi-cs)
 * Created: 2025-11-19
 * Updated: 2025-11-19
 * License: MIT License (see LICENSE file for details)
 * =====================================================================================================================
 */

package com.mobinyousefi.googlesearchfilter.ui;

import com.mobinyousefi.googlesearchfilter.exception.SearchException;
import com.mobinyousefi.googlesearchfilter.model.FilterCriteria;
import com.mobinyousefi.googlesearchfilter.model.SearchResult;
import com.mobinyousefi.googlesearchfilter.service.SearchClient;
import com.mobinyousefi.googlesearchfilter.service.SearchFilter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Simple console-based user interface.
 */
public class ConsoleUI {

    private final SearchClient searchClient;
    private final SearchFilter searchFilter;

    public ConsoleUI(SearchClient searchClient, SearchFilter searchFilter) {
        this.searchClient = searchClient;
        this.searchFilter = searchFilter;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            printBanner();
            boolean running = true;
            while (running) {
                System.out.print("\n> Enter search query (or 'quit' to exit): ");
                String query = scanner.nextLine().trim();
                if (query.equalsIgnoreCase("quit") || query.equalsIgnoreCase("exit")) {
                    running = false;
                    break;
                }
                if (query.isBlank()) {
                    System.out.println("[WARN] Query must not be empty.");
                    continue;
                }

                FilterCriteria criteria = askCriteria(scanner);

                try {
                    List<SearchResult> raw = searchClient.search(query, criteria.getMaxResults());
                    List<SearchResult> filtered = searchFilter.apply(raw, criteria);
                    printResults(filtered);
                } catch (IOException | SearchException e) {
                    System.err.println("[ERROR] Search failed: " + e.getMessage());
                }
            }
            System.out.println("\nGoodbye.");
        }
    }

    private void printBanner() {
        System.out.println("============================================================");
        System.out.println(" Google Search Engine Filter (CLI)");
        System.out.println(" Author: Mobin Yousefi  (GitHub: github.com/mobinyousefi-cs)");
        System.out.println("============================================================");
    }

    private FilterCriteria askCriteria(Scanner scanner) {
        FilterCriteria criteria = new FilterCriteria();

        System.out.print("Max results [20]: ");
        String maxStr = scanner.nextLine().trim();
        if (!maxStr.isEmpty()) {
            try {
                criteria.setMaxResults(Integer.parseInt(maxStr));
            } catch (NumberFormatException e) {
                System.out.println("[WARN] Invalid number. Using default 20.");
            }
        }

        System.out.print("Filter by from-date (ISO-8601, e.g., 2024-01-01T00:00:00Z) [skip]: ");
        String from = scanner.nextLine().trim();
        if (!from.isEmpty()) {
            try {
                criteria.setFromDate(OffsetDateTime.parse(from));
            } catch (DateTimeParseException e) {
                System.out.println("[WARN] Invalid date format. Ignoring from-date.");
            }
        }

        System.out.print("Filter by to-date (ISO-8601) [skip]: ");
        String to = scanner.nextLine().trim();
        if (!to.isEmpty()) {
            try {
                criteria.setToDate(OffsetDateTime.parse(to));
            } catch (DateTimeParseException e) {
                System.out.println("[WARN] Invalid date format. Ignoring to-date.");
            }
        }

        System.out.print("Domain whitelist (comma-separated, e.g., example.com,github.com) [skip]: ");
        String whitelist = scanner.nextLine().trim();
        if (!whitelist.isEmpty()) {
            for (String d : whitelist.split(",")) {
                String domain = d.trim();
                if (!domain.isEmpty()) {
                    criteria.getDomainWhitelist().add(domain);
                }
            }
        }

        System.out.print("Domain blacklist (comma-separated) [skip]: ");
        String blacklist = scanner.nextLine().trim();
        if (!blacklist.isEmpty()) {
            for (String d : blacklist.split(",")) {
                String domain = d.trim();
                if (!domain.isEmpty()) {
                    criteria.getDomainBlacklist().add(domain);
                }
            }
        }

        System.out.print("Restrict MIME types (comma-separated, e.g., application/pdf,text/html) [skip]: ");
        String mime = scanner.nextLine().trim();
        if (!mime.isEmpty()) {
            for (String m : mime.split(",")) {
                String mt = m.trim();
                if (!mt.isEmpty()) {
                    criteria.getMimeTypes().add(mt);
                }
            }
        }

        System.out.print("Restrict languages (comma-separated, e.g., en,fa,de) [skip]: ");
        String lang = scanner.nextLine().trim();
        if (!lang.isEmpty()) {
            for (String l : lang.split(",")) {
                String lc = l.trim();
                if (!lc.isEmpty()) {
                    criteria.getLanguageCodes().add(lc);
                }
            }
        }

        System.out.print("Safe results only? [Y/n]: ");
        String safe = scanner.nextLine().trim();
        criteria.setSafeOnly(!safe.equalsIgnoreCase("n"));

        return criteria;
    }

    private void printResults(List<SearchResult> results) {
        if (results == null || results.isEmpty()) {
            System.out.println("\n[INFO] No results matched the filter criteria.");
            return;
        }

        System.out.println("\nFiltered results (" + results.size() + "):");
        System.out.println("------------------------------------------------------------");
        int index = 1;
        for (SearchResult r : results) {
            System.out.println("#" + index++);
            if (r.getTitle() != null) {
                System.out.println("Title : " + r.getTitle());
            }
            if (r.getLink() != null) {
                System.out.println("URL   : " + r.getLink());
            }
            if (r.getDisplayLink() != null) {
                System.out.println("Host  : " + r.getDisplayLink());
            }
            if (r.getIndexedTime() != null) {
                System.out.println("Date  : " + r.getIndexedTime());
            }
            if (r.getMimeType() != null) {
                System.out.println("MIME  : " + r.getMimeType());
            }
            if (r.getLanguageCode() != null) {
                System.out.println("Lang  : " + r.getLanguageCode());
            }
            if (r.getSnippet() != null) {
                System.out.println("Snippet:\n" + r.getSnippet());
            }
            System.out.println("------------------------------------------------------------");
        }
    }
}
