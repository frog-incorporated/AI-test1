package com.hexo.tools.vision

import com.hexo.tools.AssistantTool

/**
 * VisionTool handles image understanding tasks. The default implementation
 * sends images to a vision model (e.g. Gemma) via the agent's prompt
 * builder. This stub returns a placeholder response.
 */
class VisionTool : AssistantTool {
    override val name: String = "vision"

    override suspend fun handle(input: String, extras: Map<String, Any>): String {
        // In a real implementation, `extras` would contain an image bitmap or URI
        // and this method would call the vision model to describe it.
        return "Vision processing is not implemented yet."
    }
}