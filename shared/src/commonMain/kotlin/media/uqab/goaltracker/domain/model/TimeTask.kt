package media.uqab.goaltracker.domain.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import media.uqab.goaltracker.utils.timeInMillis
import org.mongodb.kbson.ObjectId
import java.time.LocalDate


@Serializable
class TimeTask() : RealmObject {
    var id: Long = System.currentTimeMillis()
    var title: String = ""

    /**
     * Target in milliseconds
     */
    var target: Long = 0

    /**
     * Progress in Milliseconds
     */
    @Ignore
    val progress: Long
        get() = progressList.totalProgress(_type, LocalDate.now())

    val progressPercentage: Float
        get() = try {
            100f * progress / target
        } catch (ignored: Exception) {
            0f
        }

    @PrimaryKey
    var _id: ObjectId = ObjectId()

    var type: String = RepeatTypeConverter.serialize(TaskRepeatType.DAILY)
    private val _type: TaskRepeatType get() = RepeatTypeConverter.deserialize(type)

    @Transient
    var progressList: RealmList<Progress> = realmListOf()

    constructor(title: String, target: Long) : this() {
        this.title = title
        this.target = target
    }

    @Ignore
    val todayProgress: Progress? get() = progressList.firstOrNull {
        it.dateCreated == LocalDate.now().atStartOfDay().timeInMillis
    }

    fun getProgressOf(date: LocalDate): Float {
        val total = progressList.totalProgress(_type, date)
        return try {
            100f * total / target
        } catch (ignored: Exception) {
            0f
        }
    }

    override fun toString(): String {
        return Json.encodeToString(serializer(), this)
    }
}