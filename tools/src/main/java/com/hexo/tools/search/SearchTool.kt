package com.hexo.tools.search

import com.hexo.core.search.SearchClient
import com.hexo.tools.AssistantTool

/**
 * SearchTool performs web searches using DuckDuckGo HTML scraping. The
 * resulting links and titles are returned in a summarised text format.
 */
class SearchTool : AssistantTool {
    override val name: String = "search"

    override suspend fun handle(input: String, extras: Map<String, Any>): String {
        val query = input.removePrefix("search").trim().ifEmpty { return "What would you like me to search for?" }
        val results = SearchClient.search(query)
        return if (results.isEmpty()) {
            "I couldn't find any results for \"$query\"."
        } else {
            buildString {
                append("Here are the top results for \"$query\":\n")
                results.forEachIndexed { index, result ->
                    append("${index + 1}. ${result.title} - ${result.url}\n")
                }
            }
        }
    }
}