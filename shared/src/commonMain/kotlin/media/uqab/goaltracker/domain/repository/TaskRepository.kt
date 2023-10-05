package media.uqab.goaltracker.domain.repository

import media.uqab.goaltracker.domain.model.TaskRepeatType
import media.uqab.goaltracker.domain.model.Task
import media.uqab.goaltracker.domain.model.TaskType

interface TaskRepository {
    suspend fun createTimerTask(task: Task)
    suspend fun getTimerTasks(): List<Task>
    suspend fun getTask(id: Long): Task?
    suspend fun putTask(
        task: Task,
        title: String,
        type: TaskType,
        repeat: TaskRepeatType
    )

    suspend fun putProgress(task: Task, progress: Float)
    suspend fun deleteTask(id: Long)
}