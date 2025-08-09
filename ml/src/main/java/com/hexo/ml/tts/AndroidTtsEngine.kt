package com.hexo.ml.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * Text-to-speech engine using Android's [TextToSpeech]. This engine
 * initialises lazily and synthesises speech on the main thread. It emits
 * speech directly through the device's audio output.
 */
class AndroidTtsEngine(private val context: Context) : TtsEngine {
    private var tts: TextToSpeech? = null
    private var isReady = false

    private suspend fun ensureInit() = withContext(Dispatchers.Main) {
        if (tts == null) {
            tts = TextToSpeech(context) { status ->
                isReady = status == TextToSpeech.SUCCESS
                if (isReady) {
                    tts?.language = Locale.US
                }
            }
        }
    }

    override suspend fun speak(text: String) {
        ensureInit()
        if (!isReady) return
        withContext(Dispatchers.Main) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun stop() {
        tts?.stop()
    }
}