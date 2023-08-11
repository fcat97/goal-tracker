package media.uqab.goaltracker.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import media.uqab.goaltracker.data.repository.RealmTaskRepository
import media.uqab.goaltracker.domain.model.TaskRepeatType
import media.uqab.goaltracker.domain.model.TimeTask
import media.uqab.goaltracker.domain.model.UiEvent
import media.uqab.goaltracker.utils.preference.AppPreference
import media.uqab.goaltracker.utils.sound.AudioRes
import media.uqab.goaltracker.utils.sound.MediaPlayerProvider.playSound
import media.uqab.goaltracker.utils.sound.getMediaPlayer
import kotlin.time.Duration.Companion.milliseconds

class TaskEditViewModel: ViewModel {
    private val repository = RealmTaskRepository.getInstance()
    private val settings = AppPreference.getInstance()
    private val notificationSound = settings.getBoolean(AppPreference.KEY_NOTIFICATION_SOUND)

    var taskName by mutableStateOf("")
    var targetInMillis by mutableStateOf(0L)
    var hour by mutableStateOf(0)
    var minute by mutableStateOf(0)
    var type by mutableStateOf(TaskRepeatType.DAILY)
    var uiEvent by mutableStateOf<UiEvent?>(null)
    private var task: TimeTask? = null

    suspend fun saveTask(): Boolean {
        if (taskName.isEmpty() || targetInMillis == 0L) {
            uiEvent = UiEvent("Enter valid data")
            return false
        }
        repository.putTask(task ?: TimeTask(), taskName, targetInMillis, type)
        if (notificationSound) playSound(AudioRes.task_done)
        return true
    }

    suspend fun getTask(taskID: Long) {
        if (taskID == -1L) return

        task = repository.getTask(taskID)?.let {
            taskName = it.title
            it.target.milliseconds.toComponents { h, m, _, _ ->
                hour = h.toInt()
                minute = m
            }
            it
        }
    }
}