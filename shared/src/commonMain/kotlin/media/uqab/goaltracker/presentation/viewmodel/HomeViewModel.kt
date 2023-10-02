package media.uqab.goaltracker.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import media.uqab.goaltracker.data.repository.RealmTaskRepository
import media.uqab.goaltracker.domain.model.Task
import media.uqab.goaltracker.domain.model.TimeTask
import media.uqab.goaltracker.presentation.component.Entity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel: ViewModel {
    var timerTasks = mutableStateListOf<TimeTask>()
    var showingTask by mutableStateOf<List<ItemTask>>(emptyList())
    var chartEntity by mutableStateOf<List<Entity>>(emptyList())

    var currentDateProgress by mutableStateOf("0%")
    var currentDate by mutableStateOf("")
    var selectedDate: LocalDate by mutableStateOf(LocalDate.now())

    fun selectNextDate() { selectedDate = selectedDate.plusDays(1) }
    fun selectToday() { selectedDate = LocalDate.now() }
    fun selectPreviousDate() { selectedDate = selectedDate.minusDays(1) }

    suspend fun getTimerTasks() {
        val repository = RealmTaskRepository.getInstance()

        timerTasks.clear()
        repository.getTimerTasks().forEach { timerTasks.add(it) }
    }

    fun updatePercentage() {
        val formatter = DateTimeFormatter.ofPattern("EEE dd MMM, yy")
        currentDate = selectedDate.format(formatter)

        var progress = 0f
        timerTasks.forEach { progress += it.getProgressOf(selectedDate) }
        progress /= timerTasks.size
        currentDateProgress = "${String.format("%.2f", progress)} %"

        showingTask = timerTasks.map { ItemTask(it, it.title, it.getProgressOf(selectedDate)) }
        chartEntity = showingTask.mapIndexed { index, task ->
            Entity(index.toFloat(), task.percentage)
        }
    }

    data class ItemTask(val t: TimeTask, val title: String, val percentage: Float)
}