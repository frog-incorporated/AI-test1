package com.hexo.core.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

/**
 * A simple client that performs HTML scraping of DuckDuckGo to fetch
 * web search results without requiring an API key. Results are limited
 * and simplified. In a production application you may consider more
 * sophisticated scraping or caching strategies.
 */
object SearchClient {
    /**
     * Perform a search query against DuckDuckGo and return a list of
     * result titles and URLs. This call should be executed on a
     * background thread.
     */
    suspend fun search(query: String, maxResults: Int = 5): List<SearchResult> = withContext(Dispatchers.IO) {
        val encoded = java.net.URLEncoder.encode(query, Charsets.UTF_8)
        val url = "https://duckduckgo.com/html/?q=$encoded"
        val doc = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (compatible; HexoBot/1.0; +https://example.com)")
            .get()
        val results = mutableListOf<SearchResult>()
        val elements = doc.select("#links .result")
        for (el in elements.take(maxResults)) {
            val title = el.selectFirst("a.result__a")?.text()?.trim() ?: continue
            val link = el.selectFirst("a.result__a")?.attr("href") ?: continue
            results.add(SearchResult(title, link))
        }
        results
    }

    data class SearchResult(val title: String, val url: String)
}