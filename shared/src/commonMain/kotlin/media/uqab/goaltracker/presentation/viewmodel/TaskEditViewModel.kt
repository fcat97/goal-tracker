package media.uqab.goaltracker.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import media.uqab.goaltracker.data.repository.RealmTaskRepository
import media.uqab.goaltracker.domain.model.TimeTask
import media.uqab.goaltracker.domain.model.UiEvent
import kotlin.time.Duration.Companion.milliseconds

class TaskEditViewModel: ViewModel {
    private val repository = RealmTaskRepository.getInstance()
    var taskName by mutableStateOf("")
    var targetInMillis by mutableStateOf(0L)
    var hour by mutableStateOf(0)
    var minute by mutableStateOf(0)
    var uiEvent by mutableStateOf<UiEvent?>(null)
    private var task: TimeTask? = null

    suspend fun saveTask() {
        repository.putTask(task ?: TimeTask(), taskName, targetInMillis)
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