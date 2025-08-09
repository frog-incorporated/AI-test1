package com.hexo.agent.router

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * The AgentRouter decides which tool or brain to invoke given a user query.
 * It can either call a small router model hosted on Ollama or fall back
 * to heuristic routing based on keywords. The router returns one of a
 * predefined set of actions, e.g. weather, time, search, news, article,
 * brain2, brain2_reason, or none.
 */
class AgentRouter(
    private val httpClient: OkHttpClient = OkHttpClient(),
    private val ollamaHost: String = "http://127.0.0.1:11434",
    private val model: String = "qwen3:0.6b",
) {
    /**
     * Route the [question] to the appropriate tool or brain. If the
     * Ollama router is unavailable this falls back to simple keyword
     * matching.
     */
    suspend fun route(question: String): String {
        return try {
            val prompt = buildRouterPrompt(question)
            val body = JSONObject()
                .put("model", model)
                .put("prompt", prompt)
                .put("stream", false)
                .toString()
                .toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$ollamaHost/api/generate")
                .post(body)
                .build()
            val response = httpClient.newCall(request).execute()
            val json = JSONObject(response.body?.string().orEmpty())
            val content = json.optString("response").trim()
            if (content.isNotEmpty()) content else fallbackRoute(question)
        } catch (_: Exception) {
            fallbackRoute(question)
        }
    }

    private fun buildRouterPrompt(question: String): String {
        return """You are the routing engine for Hexo. Given the user's question, output one of the following actions in lowercase without additional commentary:\n" +
                "weather, time, timer, search, news, article, vision, brain2, brain2_reason, none.\n" +
                "Question: $question\n" +
                "Action:"
    }

    /**
     * Basic fallback routing based on keyword detection. This covers the
     * most common cases when the router model is unavailable.
     */
    private fun fallbackRoute(question: String): String {
        val q = question.lowercase()
        return when {
            listOf("weather", "temperature").any { q.contains(it) } -> "weather"
            listOf("time").any { q.contains(it) } -> "time"
            listOf("timer").any { q.contains(it) } -> "timer"
            listOf("news", "headline").any { q.contains(it) } -> "news"
            listOf("search", "look up").any { q.contains(it) } -> "search"
            listOf("read", "article").any { q.contains(it) } -> "article"
            listOf("image", "photo", "picture").any { q.contains(it) } -> "vision"
            else -> "none"
        }
    }
}