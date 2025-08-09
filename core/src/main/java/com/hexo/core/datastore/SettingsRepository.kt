package com.hexo.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for persisting and retrieving user settings using the
 * DataStore Preferences API. All assistant configuration options (such
 * as model selection, wake word sensitivity, STT/TTS choice, etc.) are
 * persisted via this repository.
 */
class SettingsRepository(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "hexo_settings")

    // Keys for various settings. More can be added as needed.
    private val routerModelKey = stringPreferencesKey("router_model")
    private val brain1ModelKey = stringPreferencesKey("brain1_model")
    private val brain2ModelKey = stringPreferencesKey("brain2_model")
    private val visionModelKey = stringPreferencesKey("vision_model")
    private val reasoningPolicyKey = stringPreferencesKey("reasoning_policy")
    private val sttProviderKey = stringPreferencesKey("stt_provider")
    private val ttsProviderKey = stringPreferencesKey("tts_provider")
    private val wakeWordSensitivityKey = intPreferencesKey("wake_word_sensitivity")

    /**
     * Flow of settings for consumption by the UI or assistant engine.
     */
    val settingsFlow: Flow<Settings> = context.dataStore.data.map { prefs ->
        Settings(
            routerModel = prefs[routerModelKey] ?: "qwen3:0.6b",
            brain1Model = prefs[brain1ModelKey] ?: "qwen3:1.7b",
            brain2Model = prefs[brain2ModelKey] ?: "qwen3:4b",
            visionModel = prefs[visionModelKey] ?: "gemma-3-1b-vision",
            reasoningPolicy = prefs[reasoningPolicyKey] ?: "auto",
            sttProvider = prefs[sttProviderKey] ?: "android",
            ttsProvider = prefs[ttsProviderKey] ?: "android",
            wakeWordSensitivity = prefs[wakeWordSensitivityKey] ?: 50,
        )
    }

    suspend fun updateRouterModel(value: String) {
        context.dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply { this[routerModelKey] = value }
        }
    }
    // Additional update functions omitted for brevity

    /**
     * Immutable data class representing all persisted settings.
     */
    data class Settings(
        val routerModel: String,
        val brain1Model: String,
        val brain2Model: String,
        val visionModel: String,
        val reasoningPolicy: String,
        val sttProvider: String,
        val ttsProvider: String,
        val wakeWordSensitivity: Int,
    )
}