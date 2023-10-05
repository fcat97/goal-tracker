package media.uqab.goaltracker.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class TaskType

@Serializable
class TimerTask: TaskType() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TimerTask) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

@Serializable
class TodoTask(var unit: String = "Unit"): TaskType() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TodoTask) return false

        if (unit != other.unit) return false

        return true
    }

    override fun hashCode(): Int {
        return unit.hashCode()
    }
}