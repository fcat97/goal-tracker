package media.uqab.goaltracker.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import media.uqab.goaltracker.data.repository.RealmTaskRepository
import media.uqab.goaltracker.domain.model.Daily
import media.uqab.goaltracker.domain.model.DayOfWeek
import media.uqab.goaltracker.domain.model.Task
import media.uqab.goaltracker.domain.model.TaskRepeatType
import media.uqab.goaltracker.domain.model.TaskType
import media.uqab.goaltracker.domain.model.TimerTask
import media.uqab.goaltracker.domain.model.TodoTask
import media.uqab.goaltracker.domain.model.UiEvent
import media.uqab.goaltracker.utils.preference.AppPreference
import media.uqab.goaltracker.utils.sound.AudioRes
import media.uqab.goaltracker.utils.sound.MediaPlayerProvider.playSound
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TaskEditViewModel: ViewModel {
    private val repository = RealmTaskRepository.getInstance()
    private val settings = AppPreference.getInstance()
    private val notificationSound = settings.getBoolean(AppPreference.KEY_NOTIFICATION_SOUND)

    var taskName by mutableStateOf("")

    // for time task
    var hour by mutableStateOf(0)
    var minute by mutableStateOf(0)

    // for todoTask
    var goal by mutableStateOf(1f)
    var unit by mutableStateOf("Unit")

    var type by mutableStateOf<TaskType>(TimerTask())
    var repeat by mutableStateOf<TaskRepeatType>(Daily(1f))
    var uiEvent by mutableStateOf<UiEvent?>(null)
    var selectedWeekDay by mutableStateOf(DayOfWeek.Sat)
    private var task: Task? = null

    suspend fun saveTask(): Boolean {
        when(type) {
            is TimerTask -> {
                if (taskName.isEmpty() || (hour <= 0 && minute <= 0)) {
                    uiEvent = UiEvent("Enter valid data")
                    return false
                }

                repeat.target = (hour * 60 + minute) * 60f // second
                repository.putTask(task ?: Task(), taskName, type, repeat)
                if (notificationSound) playSound(AudioRes.task_done)
                return true
            }
            else -> return false
        }
    }

    suspend fun getTask(taskID: Long) {
        if (taskID == -1L) return

        task = repository.getTask(taskID)?.let {
            taskName = it.title
            when(it.type) {
                is TimerTask -> {
                    it.repeat.target.toLong().seconds.toComponents { h, m, _, _ ->
                        hour = h.toInt()
                        minute = m
                    }
                }
                is TodoTask -> {
                    goal = it.repeat.target
                    unit = (it.type as TodoTask).unit
                }
            }

            it
        }
    }
}