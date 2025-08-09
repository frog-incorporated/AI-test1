package com.hexo.agent

import com.hexo.tools.AssistantTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ToolRunner executes the appropriate tool given its name. Tools are
 * registered via the constructor. If no tool exists for the given name
 * it returns null, signalling that the LLM should handle the request.
 */
class ToolRunner(private val tools: Map<String, AssistantTool>) {
    suspend fun runTool(name: String, input: String, extras: Map<String, Any> = emptyMap()): String? {
        val tool = tools[name] ?: return null
        return withContext(Dispatchers.Default) { tool.handle(input, extras) }
    }
}