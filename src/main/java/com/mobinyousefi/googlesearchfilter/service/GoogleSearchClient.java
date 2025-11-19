/*
 * =====================================================================================================================
 * Project: Google Search Engine Filter
 * File: GoogleSearchClient.java
 * Author: Mobin Yousefi (GitHub: https://github.com/mobinyousefi-cs)
 * Created: 2025-11-19
 * Updated: 2025-11-19
 * License: MIT License (see LICENSE file for details)
 * =====================================================================================================================
 */

package com.mobinyousefi.googlesearchfilter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobinyousefi.googlesearchfilter.exception.SearchException;
import com.mobinyousefi.googlesearchfilter.model.SearchResult;
import com.mobinyousefi.googlesearchfilter.util.Config;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Search client backed by Google Custom Search JSON API.
 *
 * You must configure an API key and search engine id (cx) in config.properties:
 *   google.apiKey=YOUR_KEY
 *   google.searchEngineId=YOUR_CX
 */
public class GoogleSearchClient implements SearchClient {

    private static final String GOOGLE_CSE_ENDPOINT = "https://www.googleapis.com/customsearch/v1";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String searchEngineId;

    public GoogleSearchClient(Config config) throws SearchException {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.apiKey = config.getRequired("google.apiKey");
        this.searchEngineId = config.getRequired("google.searchEngineId");
    }

    @Override
    public List<SearchResult> search(String query, int maxResults) throws IOException, SearchException {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("query must not be null or blank");
        }
        if (maxResults <= 0) {
            throw new IllegalArgumentException("maxResults must be positive");
        }

        int pageSize = Math.min(maxResults, 10); // Google CSE max results per request
        int start = 1;
        List<SearchResult> all = new ArrayList<>();

        while (all.size() < maxResults) {
            int remaining = maxResults - all.size();
            int num = Math.min(pageSize, remaining);

            String url = buildUrl(query, start, num);
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();

            HttpResponse<String> response;
            try {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SearchException("Search request interrupted", e);
            }

            if (response.statusCode() != 200) {
                throw new SearchException("Non-200 response from Google: " + response.statusCode());
            }

            List<SearchResult> page = parseResults(response.body());
            if (page.isEmpty()) {
                break; // no more results
            }

            all.addAll(page);
            start += pageSize;
        }

        return all.size() > maxResults ? all.subList(0, maxResults) : all;
    }

    private String buildUrl(String query, int start, int num) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return GOOGLE_CSE_ENDPOINT +
                "?key=" + apiKey +
                "&cx=" + searchEngineId +
                "&q=" + encodedQuery +
                "&start=" + start +
                "&num=" + num;
    }

    private List<SearchResult> parseResults(String jsonBody) throws IOException {
        List<SearchResult> results = new ArrayList<>();
        JsonNode root = objectMapper.readTree(jsonBody);
        JsonNode items = root.get("items");
        if (items == null || !items.isArray()) {
            return results;
        }

        for (JsonNode item : items) {
            SearchResult.Builder builder = SearchResult.builder()
                    .title(textOrNull(item.get("title")))
                    .link(textOrNull(item.get("link")))
                    .displayLink(textOrNull(item.get("displayLink")))
                    .snippet(textOrNull(item.get("snippet")));

            JsonNode mimeTypeNode = item.get("mime");
            if (mimeTypeNode != null) {
                builder.mimeType(mimeTypeNode.asText());
            }

            JsonNode fileFormatNode = item.get("fileFormat");
            if (fileFormatNode != null) {
                builder.fileFormat(fileFormatNode.asText());
            }

            JsonNode pagemap = item.get("pagemap");
            if (pagemap != null && pagemap.has("metatags")) {
                JsonNode metatagsArray = pagemap.get("metatags");
                if (metatagsArray.isArray() && !metatagsArray.isEmpty()) {
                    JsonNode meta = metatagsArray.get(0);

                    // try to infer date
                    OffsetDateTime indexedTime = parseDate(meta, "article:published_time", "og:updated_time", "date");
                    builder.indexedTime(indexedTime);

                    // try to infer language
                    String lang = textOrNull(meta.get("og:locale"));
                    if (lang != null) {
                        builder.languageCode(lang);
                    }
                }
            }

            // CSE does not expose a direct "safe" flag here; we assume safe search is configured in the engine.
            builder.safe(true);

            results.add(builder.build());
        }
        return results;
    }

    private OffsetDateTime parseDate(JsonNode meta, String... keys) {
        for (String key : keys) {
            JsonNode node = meta.get(key);
            if (node != null && node.isTextual()) {
                try {
                    return OffsetDateTime.parse(node.asText());
                } catch (DateTimeParseException ignored) {
                    // best-effort only
                }
            }
        }
        return null;
    }

    private String textOrNull(JsonNode node) {
        return node != null && node.isTextual() ? node.asText() : null;
    }
}
