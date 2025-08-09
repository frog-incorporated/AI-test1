package com.hexo.agent.speech

import com.hexo.ml.tts.TtsEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * StreamingSpeaker breaks up long responses into sentences and speaks
 * them sequentially using a [TtsEngine]. By streaming sentence-by-sentence
 * the user receives feedback sooner, and the assistant can interrupt
 * synthesis if a new wake word is detected.
 */
class StreamingSpeaker(private val tts: TtsEngine) {
    @Volatile
    private var cancelled: Boolean = false

    suspend fun speak(response: String) {
        cancelled = false
        val sentences = response.split(Regex("(?<=[.!?])\s+"))
        for (sentence in sentences) {
            if (cancelled) break
            tts.speak(sentence)
        }
    }

    fun cancel() {
        cancelled = true
        tts.stop()
    }
}