package com.hexo.ml.stt

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Interface for speech-to-text engines. Implementations should start
 * listening to the microphone upon request and emit transcribed text when
 * speech is complete. Clients are responsible for handling permissions and
 * microphone acquisition.
 */
interface SttEngine {
    /** Start listening and emit transcribed utterances via [transcriptions]. */
    fun startListening()

    /** Stop listening. */
    fun stopListening()

    /** Flow of transcription results. */
    val transcriptions: Flow<String>
}

/**
 * Stub implementation of [SttEngine] that does not perform real speech
 * recognition. Instead it exposes a function to manually emit a transcription.
 * Use the AndroidSpeechRecognizerStt in production for on-device STT.
 */
class StubSttEngine : SttEngine {
    private val _transcriptions = MutableSharedFlow<String>()
    override val transcriptions = _transcriptions.asSharedFlow()

    override fun startListening() {
        // Start listening using SpeechRecognizer or Whisper.cpp. Not implemented.
    }

    override fun stopListening() {
        // Stop listening. Not implemented.
    }

    /** Simulate a transcription result for testing. */
    suspend fun emitTranscription(text: String) {
        _transcriptions.emit(text)
    }
}