package com.hexo.agent

import com.hexo.agent.brain.BrainClient
import com.hexo.agent.router.AgentRouter
import com.hexo.agent.speech.StreamingSpeaker
import com.hexo.ml.stt.SttEngine
import com.hexo.ml.tts.TtsEngine
import com.hexo.ml.wakeword.WakeWordEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * HexoAssistant is the central orchestrator tying together the wake-word
 * detector, speech-to-text, router, tools, brain models, and text-to-speech.
 * Once started it listens for the wake word, processes user requests, and
 * speaks responses back to the user. It is designed to run within a
 * lifecycle-aware component such as a Service or ViewModel.
 */
class HexoAssistant(
    private val wakeWordEngine: WakeWordEngine,
    private val sttEngine: SttEngine,
    ttsEngine: TtsEngine,
    private val router: AgentRouter,
    private val toolRunner: ToolRunner,
    private val brainClient: BrainClient,
    private val brain1Model: String = "qwen3:1.7b",
    private val brain2Model: String = "qwen3:4b",
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val speaker = StreamingSpeaker(ttsEngine)

    /** Start the assistant. Begins listening for wake word detections. */
    fun start() {
        wakeWordEngine.start()
        scope.launch {
            wakeWordEngine.wakeWordDetections.collect {
                // Interrupt any current TTS and start new session
                speaker.cancel()
                handleSession()
            }
        }
    }

    /** Stop all ongoing operations and release resources. */
    fun stop() {
        wakeWordEngine.stop()
        sttEngine.stopListening()
        speaker.cancel()
        scope.cancel()
    }

    /**
     * Handles a single interaction session: listen for the user's question,
     * route it, invoke tools or brain models, and speak the result.
     */
    private suspend fun handleSession() {
        // Listen for an utterance
        sttEngine.startListening()
        val query = sttEngine.transcriptions.first().trim()
        sttEngine.stopListening()
        if (query.isBlank()) return
        // Route the query
        val action = withContext(Dispatchers.IO) { router.route(query) }
        val response: String = when (action) {
            "none" -> {
                // Delegate to brain1
                brainClient.chat(brain1Model, query, reasoning = false)
            }
            "brain2" -> {
                brainClient.chat(brain2Model, query, reasoning = false)
            }
            "brain2_reason" -> {
                brainClient.chat(brain2Model, query, reasoning = true)
            }
            else -> {
                // Tools or chained actions
                val toolResult = toolRunner.runTool(action, query)
                toolResult ?: brainClient.chat(brain1Model, query, reasoning = false)
            }
        }
        // Speak the response
        speaker.speak(response)
    }
}