package media.uqab.goaltracker.domain.model

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import media.uqab.goaltracker.utils.sumOf
import media.uqab.goaltracker.utils.timeInMillis
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
sealed interface TaskRepeatType {
    var target: Float

    /**
     * Get progress of a period
     *
     * @param progress List of progress
     * @param date period's end time to consider. This date will be used to find the
     * period's starting time
     */
    fun progressOf(progress: List<Progress>, time: LocalDateTime): Float

    fun percentage(progress: List<Progress>, time: LocalDateTime): Float {
        return try {
            progressOf(progress, time) / target
        } catch (_: Exception) {
            0f
        }
    }
}

private var i = 0

@Composable
fun TaskRepeatType.name(): String {
    println("TaskRepeatType called: ${++i}")

    return when (this) {
        is Daily -> "Daily"
        is Weekly -> "Weekly"
        is Monthly -> "Monthly"
        is Yearly -> "Yearly"
        else -> "Not Supported"
    }
}

@Serializable
class Daily(override var target: Float) : TaskRepeatType {

    override fun progressOf(progress: List<Progress>, time: LocalDateTime): Float {
        val dateStart = time
            .toLocalDate()
            .atStartOfDay()
            .timeInMillis

        val dateEnd = dateStart + 86400_000

        return progress.filter { it.dateCreated in dateStart until dateEnd }
            .sumOf { it.progress }
    }
}

@Serializable
class Weekly(
    override var target: Float,
    var starting: WeekDay = WeekDay(DayOfWeek.Sat)
) : TaskRepeatType {

    /**
     * Find the previous starting of current period
     */
    private fun periodStarting(): LocalDate {
        val st = starting.toDayOfWeek()

        var today = LocalDate.now()
        while (today.dayOfWeek != st) {
            today = today.minusDays(1)
        }
        return today
    }

    override fun progressOf(progress: List<Progress>, time: LocalDateTime): Float {
        val start = periodStarting().atStartOfDay().timeInMillis
        val end = time.timeInMillis

        return progress.filter {
            it.dateCreated in start until end
        }.sumOf { it.progress }
    }
}

@Serializable
class Monthly(
    override var target: Float,
    var starting: DateOfMonth = DateOfMonth(1)
) : TaskRepeatType {
    /**
     * Find the previous starting of current period
     */
    private fun periodStarting(): LocalDate {
        return LocalDate.now().withDayOfMonth(starting.dateOfMonth)
    }

    override fun progressOf(progress: List<Progress>, time: LocalDateTime): Float {
        val start = periodStarting().atStartOfDay().timeInMillis
        val end = time.timeInMillis

        return progress.filter {
            it.dateCreated in start until end
        }.sumOf { it.progress }
    }
}

@Serializable
class Yearly(
    override var target: Float,
    var starting: DateOfYear = DateOfYear(Month(MonthOfYear.Jan), DateOfMonth(1))
) : TaskRepeatType {
    /**
     * Find the previous starting of current period
     */
    private fun periodStarting(): LocalDate {
        return LocalDate.now().apply {
            withMonth(starting.month.toMonth().value)
            withDayOfMonth(starting.dateOfMonth.dateOfMonth)
        }
    }

    override fun progressOf(progress: List<Progress>, time: LocalDateTime): Float {
        val start = periodStarting().atStartOfDay().timeInMillis
        val end = time.timeInMillis

        return progress.filter {
            it.dateCreated in start until end
        }.sumOf { it.progress }
    }
}

@Serializable
class OnEveryNDays(
    var starting: TaskStarting, var duration: Int,
    override var target: Float
) : TaskRepeatType {

    override fun progressOf(progress: List<Progress>, time: LocalDateTime): Float {
        TODO("Not yet implemented")
    }
}

@Serializable
class OnceComplete(
    override var target: Float
) : TaskRepeatType {
    override fun progressOf(progress: List<Progress>, time: LocalDateTime): Float {
        TODO("Not yet implemented")
    }
}