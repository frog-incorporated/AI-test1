package com.hexo.tools

/**
 * Base interface for all assistant tools. Each tool is responsible for
 * handling a specific type of user request. The [handle] function should
 * return a human-readable response that can be fed back to the LLM or
 * spoken directly. Tools may use dependencies from the core and ml
 * modules via constructor injection.
 */
interface AssistantTool {
    val name: String
    suspend fun handle(input: String, extras: Map<String, Any> = emptyMap()): String
}