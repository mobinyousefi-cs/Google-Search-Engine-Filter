/*
 * =====================================================================================================================
 * Project: Google Search Engine Filter
 * File: GoogleSearchFilterApp.java
 * Author: Mobin Yousefi (GitHub: https://github.com/mobinyousefi-cs)
 * Created: 2025-11-19
 * Updated: 2025-11-19
 * License: MIT License (see LICENSE file for details)
 * =====================================================================================================================
 */

package com.mobinyousefi.googlesearchfilter;

import com.mobinyousefi.googlesearchfilter.exception.SearchException;
import com.mobinyousefi.googlesearchfilter.model.FilterCriteria;
import com.mobinyousefi.googlesearchfilter.model.SearchResult;
import com.mobinyousefi.googlesearchfilter.service.GoogleSearchClient;
import com.mobinyousefi.googlesearchfilter.service.SearchClient;
import com.mobinyousefi.googlesearchfilter.service.SearchFilter;
import com.mobinyousefi.googlesearchfilter.ui.ConsoleUI;
import com.mobinyousefi.googlesearchfilter.util.Config;

import java.io.IOException;
import java.util.List;

/**
 * Application entry point. Wires configuration, search client, filtering layer, and console UI together.
 */
public class GoogleSearchFilterApp {

    public static void main(String[] args) {
        try {
            Config config = new Config("config.properties");
            SearchClient searchClient = new GoogleSearchClient(config);
            SearchFilter searchFilter = new SearchFilter();
            ConsoleUI consoleUI = new ConsoleUI(searchClient, searchFilter);

            consoleUI.start();
        } catch (IOException e) {
            System.err.println("[FATAL] Failed to load configuration: " + e.getMessage());
        } catch (SearchException e) {
            System.err.println("[FATAL] Search engine initialization failed: " + e.getMessage());
        }
    }

    /**
     * Convenience method used by tests or future GUI integrations.
     */
    public static List<SearchResult> executeFilteredSearch(String query, FilterCriteria criteria)
            throws IOException, SearchException {
        Config config = new Config("config.properties");
        SearchClient searchClient = new GoogleSearchClient(config);
        SearchFilter searchFilter = new SearchFilter();
        List<SearchResult> raw = searchClient.search(query, criteria.getMaxResults());
        return searchFilter.apply(raw, criteria);
    }
}
