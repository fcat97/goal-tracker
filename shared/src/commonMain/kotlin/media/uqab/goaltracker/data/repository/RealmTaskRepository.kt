package media.uqab.goaltracker.data.repository

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.serialization.json.Json
import media.uqab.goaltracker.domain.model.Progress
import media.uqab.goaltracker.domain.model.TaskRepeatType
import media.uqab.goaltracker.domain.model.TimeTask
import media.uqab.goaltracker.domain.model.getMidnightTime
import media.uqab.goaltracker.domain.repository.TaskRepository

class RealmTaskRepository private constructor(private val realm: Realm) : TaskRepository {
    companion object {
        private var instance: RealmTaskRepository? = null

        @Synchronized
        fun getInstance(): RealmTaskRepository {
            if (instance == null) {
                val config = RealmConfiguration.create(setOf(TimeTask::class, Progress::class))
                val realm = Realm.open(config)
                instance = RealmTaskRepository(realm)
            }

            return instance!!
        }
    }


    override suspend fun createTimerTask(task: TimeTask) {
        realm.write {
            copyToRealm(task)
        }
    }

    override suspend fun getTask(id: Long): TimeTask? {
        return realm.query(TimeTask::class, "id = $0", id).first().find()
    }

    override suspend fun putTask(task: TimeTask, title: String, target: Long, type: TaskRepeatType) {
        realm.write {
            val t = query(TimeTask::class, "id = $0", task.id).first().find()
            if (t == null) {
                copyToRealm(TimeTask().apply {
                    this.title = title
                    this.target = target
                    this.type = Json.encodeToString(TaskRepeatType.serializer(), type)
                })
            } else {
                t.title = title
                t.target = target
                t.type = Json.encodeToString(TaskRepeatType.serializer(), type)
            }
        }
    }

    override suspend fun putProgress(task: TimeTask, progress: Long) {
        realm.write {
            findLatest(task)?.let { task ->
                var p = task.todayProgress

                if (p != null) {
                    p.progress = progress
                } else {
                    p = Progress().apply {
                        this.task = task
                        this.progress = progress
                    }
                    task.progressList.add(p)
                }
            }
        }
    }

    override suspend fun deleteItem(id: Long) {
        realm.write {
            val t = query(TimeTask::class, "id == $0", id).first().find()
            if (t != null) {
                val p = query(Progress::class, "task.id == $0", t.id).find()
                delete(p)

                findLatest(t)?.let { delete(it) }
            }
        }
    }

    override suspend fun getTimerTasks(): List<TimeTask> {
        return realm.query(TimeTask::class).find().toList()
    }

    suspend fun getProgress(): Long {
        return realm.query(Progress::class).count().find()
    }
}