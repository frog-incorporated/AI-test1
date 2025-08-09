package com.hexo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.hexo.R
import com.hexo.agent.HexoAssistant
import com.hexo.agent.ToolRunner
import com.hexo.agent.brain.BrainClient
import com.hexo.agent.router.AgentRouter
import com.hexo.core.timer.TimerManager
import com.hexo.ml.stt.AndroidSpeechRecognizerStt
import com.hexo.ml.stt.SttEngine
import com.hexo.ml.tts.AndroidTtsEngine
import com.hexo.ml.wakeword.StubWakeWordEngine
import com.hexo.tools.article.ArticleTool
import com.hexo.tools.location.LocationTool
import com.hexo.tools.news.NewsTool
import com.hexo.tools.search.SearchTool
import com.hexo.tools.time.TimeTool
import com.hexo.tools.timer.TimerTool
import com.hexo.tools.vision.VisionTool
import com.hexo.tools.weather.WeatherTool

/**
 * Foreground service hosting the Hexo assistant. This service runs in the
 * background while the application is active and keeps the wake word
 * detector, STT, and TTS alive. When the service is started it
 * initialises the [HexoAssistant] and begins listening. If the service
 * stops it cleans up its resources.
 */
class HexoService : Service() {
    private lateinit var assistant: HexoAssistant

    override fun onCreate() {
        super.onCreate()
        // Initialise ML components
        val wakeWordEngine = StubWakeWordEngine()
        val sttEngine: SttEngine = AndroidSpeechRecognizerStt(this)
        val ttsEngine = AndroidTtsEngine(this)
        val router = AgentRouter()
        val timerManager = TimerManager(this)
        // Register tools
        val tools = mapOf(
            "weather" to WeatherTool(),
            "time" to TimeTool(),
            "timer" to TimerTool(timerManager),
            "search" to SearchTool(),
            "news" to NewsTool(),
            "article" to ArticleTool(),
            "vision" to VisionTool(),
            "location" to LocationTool(this)
        )
        val toolRunner = ToolRunner(tools)
        val brainClient = BrainClient()
        assistant = HexoAssistant(
            wakeWordEngine,
            sttEngine,
            ttsEngine,
            router,
            toolRunner,
            brainClient
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = buildNotification()
        startForeground(1, notification)
        assistant.start()
        return START_STICKY
    }

    override fun onDestroy() {
        assistant.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Hexo Assistant",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Hexo voice assistant running"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Hexo Assistant")
            .setContentText("Listening for \"Hexo\"...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "hexo_assistant"
    }
}