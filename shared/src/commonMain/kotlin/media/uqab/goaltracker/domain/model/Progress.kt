package media.uqab.goaltracker.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import media.uqab.goaltracker.utils.timeInMillis
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

@Serializable
class Progress : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()

    var id: Long = System.currentTimeMillis()

    @SerialName("date_created")
    var dateCreated: Long = LocalDate.now().atStartOfDay().timeInMillis

    var progress: Long = 0L

    // foreign key, 1-1 relation
    var task: TimeTask? = null

    override fun toString(): String {
        return "Progress(_id=$_id, id=$id, dateCreated=$dateCreated, progress=$progress, task=$task)"
    }

    companion object {
        fun new(task: TimeTask, progress: Long): Progress {
            return Progress().apply {
                this.task = task
                this.progress = progress
            }
        }
    }
}

fun List<Progress>.totalProgress(type: TaskRepeatType, date: LocalDate): Long {
    return when (type) {
        TaskRepeatType.DAILY -> {
            val dateStart = date
                .atStartOfDay()
                .timeInMillis

            val dateEnd = dateStart + 86400_000

            filter { it.dateCreated in dateStart until dateEnd }.sumOf {
                it.progress
            }
        }

        else -> {
            this.sumOf { it.progress }
        }
    }
}