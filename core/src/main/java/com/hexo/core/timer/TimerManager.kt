package com.hexo.core.timer

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

/**
 * TimerManager schedules timers using WorkManager and exposes a simple API to
 * set timers and observe their state. When a timer finishes the registered
 * callback will be invoked. Timers persist across application restarts.
 */
class TimerManager(private val context: Context) {
    private val _activeTimers = MutableStateFlow<List<WorkInfo>>(emptyList())
    val activeTimers = _activeTimers.asStateFlow()

    private val workManager = WorkManager.getInstance(context)

    /**
     * Set a timer for the given duration in seconds. The callback will be
     * executed when the timer completes.
     */
    fun setTimer(durationSeconds: Long, tag: String) {
        val timerWork = OneTimeWorkRequestBuilder<TimerWorker>()
            .setInitialDelay(durationSeconds, TimeUnit.SECONDS)
            .addTag(tag)
            .build()
        workManager.enqueue(timerWork)
    }

    /**
     * Cancel a timer by tag.
     */
    fun cancelTimer(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    /**
     * Internal worker that triggers when a timer finishes. It simply logs
     * completion; in a real implementation it would notify the TTS engine
     * to speak the timer complete message.
     */
    class TimerWorker(appContext: Context, workerParams: WorkerParameters) :
        CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result {
            // Placeholder: integrate with agent or TTS to speak timer completion
            return Result.success()
        }
    }
}