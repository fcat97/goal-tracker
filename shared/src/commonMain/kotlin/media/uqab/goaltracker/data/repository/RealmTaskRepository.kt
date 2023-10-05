package media.uqab.goaltracker.data.repository

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.serialization.json.Json
import media.uqab.goaltracker.data.model.realm.RealmProgress
import media.uqab.goaltracker.data.model.realm.RealmTask
import media.uqab.goaltracker.domain.model.Progress
import media.uqab.goaltracker.domain.model.TaskRepeatType
import media.uqab.goaltracker.domain.model.Task
import media.uqab.goaltracker.domain.model.TaskType
import media.uqab.goaltracker.domain.repository.TaskRepository
import media.uqab.goaltracker.utils.timeInMillis
import java.time.LocalDate

class RealmTaskRepository private constructor(private val realm: Realm) : TaskRepository {
    companion object {
        private var instance: RealmTaskRepository? = null

        @Synchronized
        fun getInstance(): RealmTaskRepository {
            if (instance == null) {
                val config = RealmConfiguration.create(setOf(RealmTask::class, RealmProgress::class))
                val realm = Realm.open(config)
                instance = RealmTaskRepository(realm)
            }

            return instance!!
        }
    }


    override suspend fun createTimerTask(task: Task) {
        realm.write {
            copyToRealm(RealmTask().wrap(task))
        }
    }

    override suspend fun getTask(id: Long): Task? {
        return realm.query(RealmTask::class, "id = $0", id)
            .first()
            .find()
            ?.unwrap()
    }

    override suspend fun putTask(
        task: Task,
        title: String,
        type: TaskType,
        repeat: TaskRepeatType
    ) {
        realm.write {
            val old = query(RealmTask::class, "id = $0", task.id).first().find()
            if (old == null) {
                RealmTask()
                    .wrap(Task(title, type, repeat))
                    .let { copyToRealm(it) }
            } else {
                val t = old.unwrap().let {
                    it.title = title
                    it.type = type
                    it.repeat = repeat
                    it
                }

                old.wrap(t)
            }
        }
    }

    override suspend fun putProgress(task: Task, progress: Float) {
        realm.write {
            val old = query(RealmTask::class, "id = $0", task.id)
                .first()
                .find() ?: return@write

            // find existing progress
            val existing = old.progressList.find {
                it.unwrap().dateCreated == LocalDate.now().atStartOfDay().timeInMillis
            }

            // update today's progress if present, create new otherwise
            val p = existing?.unwrap() ?: Progress()
            p.progress = progress

            if (existing == null) {
                old.progressList.add(RealmProgress.new(old, p))
            } else {
                existing.wrap(p)
            }
        }
    }

    override suspend fun deleteTask(id: Long) {
        realm.write {
            val t = query(RealmTask::class, "id == $0", id).first().find()
            if (t != null) {
                val p = query(RealmProgress::class, "task.id == $0", t.id).find()
                delete(p)

                findLatest(t)?.let { delete(it) }
            }
        }
    }

    override suspend fun getTimerTasks(): List<Task> {
        return realm.query(RealmTask::class).find()
            .toList()
            .map(RealmTask::unwrap)
    }
}