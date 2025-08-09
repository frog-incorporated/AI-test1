package com.hexo.ml.wakeword

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Interface for wake-word engines. Implementations should listen to the
 * device microphone continuously and emit an event when the specified
 * keyword is detected. The engine should be low-latency and low-power.
 */
interface WakeWordEngine {
    /** Start listening for the wake word. */
    fun start()

    /** Stop listening for the wake word. */
    fun stop()

    /** Adjust the detection sensitivity. A higher value increases recall at the
     * expense of false positives. Range is implementation defined. */
    fun setSensitivity(value: Float)

    /** Flow that emits whenever the wake word is detected. */
    val wakeWordDetections: kotlinx.coroutines.flow.Flow<Unit>
}

/**
 * A basic implementation using a stubbed model. In a production build this
 * would load a TFLite model (e.g. OpenWakeWord) and process audio in a
 * separate thread. Here we simulate detection by exposing a function to
 * manually trigger detection for testing.
 */
class StubWakeWordEngine : WakeWordEngine {
    private val _flow = MutableSharedFlow<Unit>()
    override val wakeWordDetections = _flow.asSharedFlow()

    override fun start() {
        // Start capturing audio and listening for the wake word. Not implemented.
    }

    override fun stop() {
        // Stop capturing audio. Not implemented.
    }

    override fun setSensitivity(value: Float) {
        // Adjust sensitivity. No-op in stub.
    }

    /**
     * Simulate a wake-word detection. Can be called from tests or debug UI.
     */
    suspend fun simulateDetection() {
        _flow.emit(Unit)
    }
}