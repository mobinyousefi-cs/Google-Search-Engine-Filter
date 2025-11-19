/*
 * =====================================================================================================================
 * Project: Google Search Engine Filter
 * File: FilterCriteria.java
 * Author: Mobin Yousefi (GitHub: https://github.com/mobinyousefi-cs)
 * Created: 2025-11-19
 * Updated: 2025-11-19
 * License: MIT License (see LICENSE file for details)
 * =====================================================================================================================
 */

package com.mobinyousefi.googlesearchfilter.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Value object representing various filters that can be applied to a list of search results.
 */
public class FilterCriteria {

    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
    private final Set<String> domainWhitelist = new HashSet<>();
    private final Set<String> domainBlacklist = new HashSet<>();
    private final Set<String> mimeTypes = new HashSet<>();
    private final Set<String> languageCodes = new HashSet<>();
    private boolean safeOnly = true;
    private int maxResults = 20;

    public OffsetDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(OffsetDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public OffsetDateTime getToDate() {
        return toDate;
    }

    public void setToDate(OffsetDateTime toDate) {
        this.toDate = toDate;
    }

    public Set<String> getDomainWhitelist() {
        return domainWhitelist;
    }

    public Set<String> getDomainBlacklist() {
        return domainBlacklist;
    }

    public Set<String> getMimeTypes() {
        return mimeTypes;
    }

    public Set<String> getLanguageCodes() {
        return languageCodes;
    }

    public boolean isSafeOnly() {
        return safeOnly;
    }

    public void setSafeOnly(boolean safeOnly) {
        this.safeOnly = safeOnly;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        if (maxResults <= 0) {
            throw new IllegalArgumentException("maxResults must be positive");
        }
        this.maxResults = maxResults;
    }
}
