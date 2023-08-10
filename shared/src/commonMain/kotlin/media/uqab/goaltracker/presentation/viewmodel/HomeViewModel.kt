package media.uqab.goaltracker.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import media.uqab.goaltracker.data.repository.RealmTaskRepository
import media.uqab.goaltracker.domain.model.TimeTask

class HomeViewModel: ViewModel {
    var timerTasks = mutableStateListOf<TimeTask>()

    suspend fun getTimerTasks() {
        val repository = RealmTaskRepository.getInstance()

        timerTasks.clear()
        repository.getTimerTasks().forEach {
            timerTasks.add(it)
        }
        println("total progress: " +  repository.getProgress())
    }
}