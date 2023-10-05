package media.uqab.goaltracker.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import media.uqab.goaltracker.utils.sumOf
import media.uqab.goaltracker.utils.timeInMillis
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

@Serializable
class Progress {
    @SerialName("date_created")
    var dateCreated: Long = LocalDate.now().atStartOfDay().timeInMillis

    var progress: Float = 0f
}