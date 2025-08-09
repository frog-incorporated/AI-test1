package com.hexo.ml.tts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Interface for text-to-speech engines. Implementations should take a
 * string of text and synthesize speech on the device. They should be
 * interruptible and cancellable.
 */
interface TtsEngine {
    /** Speak the given [text] asynchronously. Implementations should return
     * immediately and perform synthesis in the background. */
    suspend fun speak(text: String)

    /** Stop any ongoing synthesis. */
    fun stop()
}

/**
 * Stub implementation of [TtsEngine] that does not produce audio. Instead it
 * simply logs the text or could be extended to update a UI element.
 */
class StubTtsEngine : TtsEngine {
    override suspend fun speak(text: String) {
        // In the stub we just delay to simulate speech. Replace with Android
        // TextToSpeech or Piper synthesis.
        withContext(Dispatchers.IO) {
            println("[Hexo TTS] $text")
        }
    }

    override fun stop() {
        // Stop current synthesis. No-op in stub.
    }
}