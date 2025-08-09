package com.hexo.agent.brain

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

/**
 * BrainClient communicates with Ollama to obtain natural language
 * responses. It supports two models: brain1 for general chat and brain2
 * for more complex reasoning. When `reasoning` is true, the prompt may
 * instruct the model to show its reasoning process; otherwise it should
 * respond succinctly.
 */
class BrainClient(
    private val httpClient: OkHttpClient = OkHttpClient(),
    private val ollamaHost: String = "http://127.0.0.1:11434",
) {
    suspend fun chat(model: String, prompt: String, reasoning: Boolean = false): String {
        val messages = JSONArray().apply {
            put(JSONObject().put("role", "system").put("content", "You are Hexo, a helpful voice assistant."))
            put(JSONObject().put("role", "user").put("content", prompt))
        }
        val body = JSONObject()
            .put("model", model)
            .put("messages", messages)
            .put("stream", false)
            .toString()
            .toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("$ollamaHost/api/chat")
            .post(body)
            .build()
        val response = httpClient.newCall(request).execute()
        val json = JSONObject(response.body?.string().orEmpty())
        return json.optString("response").trim()
    }
}