package media.uqab.goaltracker.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId
import java.util.Calendar

@Serializable
class Progress: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()

    var id: Long = System.currentTimeMillis()

    @SerialName("date_created")
    var dateCreated: Long = getMidnightTime()

    var progress: Long = 0L

    var task: TimeTask? = null

    override fun toString(): String {
        return "Progress(_id=$_id, id=$id, dateCreated=$dateCreated, progress=$progress, task=$task)"
    }
}

fun List<Progress>.totalProgress(type: TaskRepeatType): Long {
    return when(type) {
        TaskRepeatType.DAILY -> {
            val c = Calendar.getInstance()
            c.timeInMillis = getMidnightTime()
            val dateStart = c.timeInMillis

            c.add(Calendar.DATE, 1)
            c.add(Calendar.MILLISECOND, -1)

            val dateEnd = c.timeInMillis

            filter { it.dateCreated in dateStart..dateEnd }.sumOf { it.progress }
        }
        else -> {
            this.sumOf { it.progress }
        }
    }
}

fun getMidnightTime(): Long  {
    val c = Calendar.getInstance()
    c[Calendar.HOUR_OF_DAY] = 0
    c[Calendar.MINUTE] = 0
    c[Calendar.SECOND] = 0
    c[Calendar.MILLISECOND] = 0
    return c.timeInMillis
}