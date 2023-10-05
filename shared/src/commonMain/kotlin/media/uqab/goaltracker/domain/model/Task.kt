package media.uqab.goaltracker.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import media.uqab.goaltracker.utils.timeInMillis
import java.time.LocalDate
import java.time.LocalDateTime


@Serializable
class Task(
    var title: String = "",
    var type: TaskType = TodoTask(),
    var repeat: TaskRepeatType = Daily(1f)
) {
    var id: Long = System.currentTimeMillis()

    val progress: Float
        get() = repeat.progressOf(progressList, LocalDateTime.now())

    val progressPercentage: Float
        get() = repeat.percentage(progressList, LocalDateTime.now())

    @Transient
    var progressList: List<Progress> = emptyList()

    fun getProgressOf(date: LocalDate): Float {
        val d = date.atStartOfDay().plusDays(1)
        return repeat.progressOf(progressList, d)
    }

    fun getPercentageOf(date: LocalDate): Float {
        return 100 * repeat.percentage(progressList, date.plusDays(1).atStartOfDay())
    }

    override fun toString() = encode(this)
}