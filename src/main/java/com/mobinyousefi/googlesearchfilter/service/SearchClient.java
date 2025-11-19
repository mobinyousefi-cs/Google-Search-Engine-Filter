/*
 * =====================================================================================================================
 * Project: Google Search Engine Filter
 * File: SearchClient.java
 * Author: Mobin Yousefi (GitHub: https://github.com/mobinyousefi-cs)
 * Created: 2025-11-19
 * Updated: 2025-11-19
 * License: MIT License (see LICENSE file for details)
 * =====================================================================================================================
 */

package com.mobinyousefi.googlesearchfilter.service;

import com.mobinyousefi.googlesearchfilter.exception.SearchException;
import com.mobinyousefi.googlesearchfilter.model.SearchResult;

import java.io.IOException;
import java.util.List;

/**
 * Abstraction over any search provider (Google, Bing, offline index, etc.).
 */
public interface SearchClient {

    /**
     * Sends the query to the underlying search provider.
     *
     * @param query      free-text search query
     * @param maxResults maximum number of results desired
     * @return list of search results (can be empty, never null)
     */
    List<SearchResult> search(String query, int maxResults) throws IOException, SearchException;
}
