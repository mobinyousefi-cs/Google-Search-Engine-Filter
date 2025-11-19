/*
 * =====================================================================================================================
 * Project: Google Search Engine Filter
 * File: SearchFilter.java
 * Author: Mobin Yousefi (GitHub: https://github.com/mobinyousefi-cs)
 * Created: 2025-11-19
 * Updated: 2025-11-19
 * License: MIT License (see LICENSE file for details)
 * =====================================================================================================================
 */

package com.mobinyousefi.googlesearchfilter.service;

import com.mobinyousefi.googlesearchfilter.model.FilterCriteria;
import com.mobinyousefi.googlesearchfilter.model.SearchResult;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Pure in-memory filtering layer.
 */
public class SearchFilter {

    public List<SearchResult> apply(List<SearchResult> input, FilterCriteria criteria) {
        if (input == null || input.isEmpty()) {
            return List.of();
        }
        if (criteria == null) {
            return List.copyOf(input);
        }

        List<SearchResult> tmp = new ArrayList<>();
        for (SearchResult result : input) {
            if (!matches(result, criteria)) {
                continue;
            }
            tmp.add(result);
            if (tmp.size() >= criteria.getMaxResults()) {
                break;
            }
        }
        return tmp;
    }

    private boolean matches(SearchResult r, FilterCriteria c) {
        if (r == null) {
            return false;
        }

        // Date window
        OffsetDateTime indexed = r.getIndexedTime();
        if (indexed != null) {
            if (c.getFromDate() != null && indexed.isBefore(c.getFromDate())) {
                return false;
            }
            if (c.getToDate() != null && indexed.isAfter(c.getToDate())) {
                return false;
            }
        }

        // Domain filters
        String domain = r.getDisplayLink();
        if (!c.getDomainWhitelist().isEmpty() && domain != null) {
            boolean matchesWhitelist = c.getDomainWhitelist().stream()
                    .anyMatch(d -> domain.equalsIgnoreCase(d) || domain.toLowerCase(Locale.ROOT).endsWith("." + d.toLowerCase(Locale.ROOT)));
            if (!matchesWhitelist) {
                return false;
            }
        }

        if (!c.getDomainBlacklist().isEmpty() && domain != null) {
            boolean inBlacklist = c.getDomainBlacklist().stream()
                    .anyMatch(d -> domain.equalsIgnoreCase(d) || domain.toLowerCase(Locale.ROOT).endsWith("." + d.toLowerCase(Locale.ROOT)));
            if (inBlacklist) {
                return false;
            }
        }

        // Mime types
        if (!c.getMimeTypes().isEmpty()) {
            String mime = r.getMimeType();
            if (mime == null || c.getMimeTypes().stream().noneMatch(m -> m.equalsIgnoreCase(mime))) {
                return false;
            }
        }

        // Languages (best-effort; languageCode is optional)
        if (!c.getLanguageCodes().isEmpty()) {
            String lang = r.getLanguageCode();
            if (lang == null) {
                return false;
            }
            String norm = lang.toLowerCase(Locale.ROOT);
            boolean match = c.getLanguageCodes().stream()
                    .map(l -> l.toLowerCase(Locale.ROOT))
                    .anyMatch(norm::startsWith); // matches "en" with "en-US"
            if (!match) {
                return false;
            }
        }

        // Safe content
        if (c.isSafeOnly() && !r.isSafe()) {
            return false;
        }

        return true;
    }
}
