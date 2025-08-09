package com.hexo.tools.timer

import com.hexo.core.timer.TimerManager
import com.hexo.tools.AssistantTool

/**
 * TimerTool sets countdown timers based on natural language input. It
 * understands simple phrases like "Set a timer for 10 minutes" and uses
 * [TimerManager] to schedule the timer. Upon completion the timer will
 * trigger a notification or speech response via the agent.
 */
class TimerTool(private val timerManager: TimerManager) : AssistantTool {
    override val name: String = "timer"

    override suspend fun handle(input: String, extras: Map<String, Any>): String {
        val minutes = Regex("(\d+)\s*minute").find(input)?.groupValues?.getOrNull(1)?.toLongOrNull()
        val seconds = Regex("(\d+)\s*second").find(input)?.groupValues?.getOrNull(1)?.toLongOrNull()
        val totalSeconds = when {
            minutes != null -> minutes * 60
            seconds != null -> seconds
            else -> null
        }
        return if (totalSeconds != null) {
            val tag = "timer_${System.currentTimeMillis()}"
            timerManager.setTimer(totalSeconds, tag)
            "Setting a timer for ${minutes ?: seconds} ${if (minutes != null) "minutes" else "seconds"}."
        } else {
            "Sorry, I couldn't understand the timer duration."
        }
    }
}