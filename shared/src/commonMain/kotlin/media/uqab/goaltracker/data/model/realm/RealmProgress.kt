package media.uqab.goaltracker.data.model.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import media.uqab.goaltracker.domain.model.Progress
import media.uqab.goaltracker.domain.model.decode
import media.uqab.goaltracker.domain.model.encode
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

@Serializable
class RealmProgress(): RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()

    /**
     * Don't use this field directly
     * This field kept public for realm.
     * use [wrap]/[unwrap] method to get actual
     */
    var data: String = encode(Progress())

    fun wrap(progress: Progress): RealmProgress {
        data = encode(progress)
        return this
    }
    fun unwrap(): Progress = decode(data)

    // foreign key, 1-1 relation
    var task: RealmTask? = null

    override fun toString() = encode(this)

    companion object {
        fun new(task: RealmTask, progress: Progress): RealmProgress {
            return RealmProgress().apply {
                this.task = task
                this.wrap(progress)
            }
        }
    }
}