/*
 * =====================================================================================================================
 * Project: Google Search Engine Filter
 * File: SearchResult.java
 * Author: Mobin Yousefi (GitHub: https://github.com/mobinyousefi-cs)
 * Created: 2025-11-19
 * Updated: 2025-11-19
 * License: MIT License (see LICENSE file for details)
 * =====================================================================================================================
 */

package com.mobinyousefi.googlesearchfilter.model;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Immutable representation of a single search result returned by Google Custom Search.
 */
public final class SearchResult {

    private final String title;
    private final String link;
    private final String displayLink;
    private final String snippet;
    private final String mimeType;
    private final String fileFormat;
    private final OffsetDateTime indexedTime;
    private final String languageCode;
    private final boolean safe;

    private SearchResult(Builder builder) {
        this.title = builder.title;
        this.link = builder.link;
        this.displayLink = builder.displayLink;
        this.snippet = builder.snippet;
        this.mimeType = builder.mimeType;
        this.fileFormat = builder.fileFormat;
        this.indexedTime = builder.indexedTime;
        this.languageCode = builder.languageCode;
        this.safe = builder.safe;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDisplayLink() {
        return displayLink;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public OffsetDateTime getIndexedTime() {
        return indexedTime;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public boolean isSafe() {
        return safe;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", displayLink='" + displayLink + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", fileFormat='" + fileFormat + '\'' +
                ", indexedTime=" + indexedTime +
                ", languageCode='" + languageCode + '\'' +
                ", safe=" + safe +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchResult that)) return false;
        return Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private String link;
        private String displayLink;
        private String snippet;
        private String mimeType;
        private String fileFormat;
        private OffsetDateTime indexedTime;
        private String languageCode;
        private boolean safe = true;

        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public Builder displayLink(String displayLink) {
            this.displayLink = displayLink;
            return this;
        }

        public Builder snippet(String snippet) {
            this.snippet = snippet;
            return this;
        }

        public Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder fileFormat(String fileFormat) {
            this.fileFormat = fileFormat;
            return this;
        }

        public Builder indexedTime(OffsetDateTime indexedTime) {
            this.indexedTime = indexedTime;
            return this;
        }

        public Builder languageCode(String languageCode) {
            this.languageCode = languageCode;
            return this;
        }

        public Builder safe(boolean safe) {
            this.safe = safe;
            return this;
        }

        public SearchResult build() {
            return new SearchResult(this);
        }
    }
}
