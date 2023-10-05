package media.uqab.goaltracker.data.model.realm

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import media.uqab.goaltracker.domain.model.Task
import media.uqab.goaltracker.domain.model.decode
import media.uqab.goaltracker.domain.model.encode
import org.jetbrains.skia.impl.Log
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

@Serializable
class RealmTask(): RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()

    var id: Long = 0

    /**
     * Don't use this field directly
     * This field kept public for realm.
     * use [wrap]/[unwrap] method to get actual
     */
    var data: String = encode(Task())
    fun wrap(task: Task): RealmTask {
        id = task.id
        data = encode(task)
        return this
    }

    fun unwrap(): Task {
        return decode<Task>(data).apply {
            id = this@RealmTask.id
            progressList = this@RealmTask.progressList.map(RealmProgress::unwrap)
        }
    }


    var progressList: RealmList<RealmProgress> = realmListOf()

    // ---------------------------------------------------
    // extra boilerplate for searching from database query


}