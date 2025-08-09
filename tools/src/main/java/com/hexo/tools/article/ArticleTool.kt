package com.hexo.tools.article

import com.hexo.tools.AssistantTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

/**
 * ArticleTool reads the full text of a news article given its URL and
 * summarises it. For simplicity, this implementation returns the first
 * few paragraphs of the article. In production you may incorporate a
 * summarisation model or a more robust readability extractor.
 */
class ArticleTool : AssistantTool {
    override val name: String = "article"

    override suspend fun handle(input: String, extras: Map<String, Any>): String {
        val url = input.trim()
        return withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(url).get()
                val paragraphs = doc.select("p").map { it.text() }
                if (paragraphs.isEmpty()) {
                    "I couldn't extract any content from that link."
                } else {
                    paragraphs.take(3).joinToString("\n\n")
                }
            } catch (e: Exception) {
                "Failed to read the article: ${e.message}"
            }
        }
    }
}