package media.uqab.goaltracker.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
enum class TaskRepeatType(val data: String) {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly"),
    IN_DAYS("1")
}

object RepeatTypeConverter {
    fun serialize(type: TaskRepeatType): String {
        return Json.encodeToString(TaskRepeatType.serializer(), type)
    }

    fun deserialize(string: String): TaskRepeatType {
        return Json.decodeFromString(TaskRepeatType.serializer(), string)
    }
}