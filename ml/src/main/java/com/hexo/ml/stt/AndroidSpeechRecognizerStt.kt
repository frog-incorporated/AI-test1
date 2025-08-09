package com.hexo.ml.stt

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Speech-to-text engine backed by Android's [SpeechRecognizer]. This class
 * listens for a single utterance after [startListening] is called and
 * emits the transcription when recognition completes. After emitting a
 * result the engine automatically stops and must be restarted for the next
 * utterance. Clients should request the RECORD_AUDIO permission before
 * starting.
 */
class AndroidSpeechRecognizerStt(private val context: Context) : SttEngine {
    private var recognizer: SpeechRecognizer? = null
    private var recognitionFlow: Flow<String>? = null

    override val transcriptions: Flow<String>
        get() = recognitionFlow ?: callbackFlow {
            if (!SpeechRecognizer.isRecognitionAvailable(context)) {
                close()
                return@callbackFlow
            }
            val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            recognizer = speechRecognizer
            val listener = object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {
                    // Send an empty result on error
                    trySend("")
                }
                override fun onResults(results: Bundle) {
                    val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val text = matches?.firstOrNull() ?: ""
                    trySend(text)
                }
                override fun onPartialResults(partialResults: Bundle) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            }
            speechRecognizer.setRecognitionListener(listener)
            awaitClose {
                speechRecognizer.setRecognitionListener(null)
                speechRecognizer.destroy()
            }
        }

    override fun startListening() {
        val recognizer = recognizer ?: return
        val intent = RecognizerIntent().apply {
            action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }
        recognizer.startListening(intent)
    }

    override fun stopListening() {
        recognizer?.stopListening()
    }
}