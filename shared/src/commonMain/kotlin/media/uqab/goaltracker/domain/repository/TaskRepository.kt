package media.uqab.goaltracker.domain.repository

import media.uqab.goaltracker.domain.model.TimeTask

interface TaskRepository {
    suspend fun createTimerTask(task: TimeTask)
    suspend fun getTimerTasks(): List<TimeTask>
    suspend fun getTask(id: Long): TimeTask?
    suspend fun putTask(task: TimeTask, title: String, target: Long)
    suspend fun putProgress(task: TimeTask, progress: Long)
    suspend fun deleteItem(id: Long)
}