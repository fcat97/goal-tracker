package media.uqab.goaltracker.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import media.uqab.goaltracker.data.repository.RealmTaskRepository
import media.uqab.goaltracker.domain.model.TimeTask
import media.uqab.goaltracker.utils.preference.AppPreference
import media.uqab.goaltracker.utils.sound.AudioRes
import media.uqab.goaltracker.utils.sound.MediaPlayerProvider.playSound
import kotlin.time.Duration.Companion.seconds

class TimerViewModel : ViewModel {
    private val repository = RealmTaskRepository.getInstance()
    private val settings = AppPreference.getInstance()
    private val notificationSound = settings.getBoolean(AppPreference.KEY_NOTIFICATION_SOUND)
    private val tickSound = settings.getBoolean(AppPreference.KEY_CLOCK_TICK_SOUND)
    var currentProgress by mutableStateOf(0)
    var currentPercent by mutableStateOf(0f)
    var taskTitle by mutableStateOf("")
    var target by mutableStateOf(1)
    var timerState by mutableStateOf(TimerState.Stopped)

    private var runningTimer: Job? = null
    private var timeTask: TimeTask? = null

    suspend fun getTask(id: Long) {
        timeTask = repository.getTask(id)?.let {
            taskTitle = it.title
            target = (it.target / 1_000L).toInt()
            currentProgress = (it.progress / 1_000).toInt()
            currentPercent = it.progressPercentage
            it
        }
    }

    suspend fun startTimer() = coroutineScope {
        runningTimer = launch {
            timerState = TimerState.Running

            while (isActive && timerState == TimerState.Running) {
                delay(1000)
                currentProgress += 1
                currentPercent = try {
                    100 * currentProgress.toFloat() / target
                } catch (ignored: Exception) { 0f }

                if (tickSound) playSound(AudioRes.clock_tick)

                if (currentProgress >= target) {
                    if (notificationSound) playSound(AudioRes.task_done)
                    stopTimer()
                }
            }
        }
    }

    suspend fun stopTimer() {
        timerState = TimerState.Stopped
        runningTimer?.cancel()
        updateTask()
    }

    suspend fun pauseTimer() {
        timerState = TimerState.Paused
        runningTimer?.cancel()
        updateTask()
    }

    private suspend fun updateTask() {
        timeTask?.let { task ->
            repository.putProgress(task, currentProgress * 1000L)
        }
    }

    suspend fun deleteTask() {
        stopTimer()
        timeTask?.let { repository.deleteTask(it.id) }
        if (notificationSound) playSound(AudioRes.task_done)
    }

    enum class TimerState {
        Stopped,
        Running,
        Paused
    }
}

fun Int.toReadable(): String {
    val sb = StringBuilder()
    this.seconds.toComponents { d, h, m, s, _ ->
        if (d > 0) sb.append("$d d ")
        if (h > 0) sb.append("$h h ")
        if (m > 0) sb.append("$m m ")
        if (s > 0) sb.append("$s s")
    }
    return sb.toString()
}