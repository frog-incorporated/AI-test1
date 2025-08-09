package com.hexo.tools.news

import com.hexo.tools.AssistantTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

/**
 * NewsTool fetches the latest headlines from a set of RSS feeds (BBC,
 * Reuters, AP) and returns them as a bullet list. Without network
 * connectivity it returns a default message.
 */
class NewsTool : AssistantTool {
    override val name: String = "news"

    private val feeds = listOf(
        "http://feeds.bbci.co.uk/news/rss.xml",
        "http://feeds.reuters.com/reuters/topNews",
        "https://www.npr.org/rss/rss.php?id=1001"
    )

    override suspend fun handle(input: String, extras: Map<String, Any>): String {
        return withContext(Dispatchers.IO) {
            val headlines = mutableListOf<String>()
            feeds.forEach { url ->
                try {
                    val doc = Jsoup.connect(url).get()
                    val items = doc.select("item > title").take(3)
                    items.forEach { elem -> headlines += elem.text() }
                } catch (_: Exception) {
                    // ignore failures; continue to next feed
                }
            }
            if (headlines.isEmpty()) {
                "I'm unable to fetch news at the moment."
            } else {
                buildString {
                    append("Here are the latest headlines:\n")
                    headlines.take(5).forEachIndexed { idx, title ->
                        append("${idx + 1}. $title\n")
                    }
                }
            }
        }
    }
}