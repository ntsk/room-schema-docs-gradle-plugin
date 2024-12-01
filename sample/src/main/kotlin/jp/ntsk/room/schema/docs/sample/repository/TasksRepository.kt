package jp.ntsk.room.schema.docs.sample.repository

import jp.ntsk.room.schema.docs.sample.database.TaskDao
import jp.ntsk.room.schema.docs.sample.model.Task
import jp.ntsk.room.schema.docs.sample.model.toEntity
import jp.ntsk.room.schema.docs.sample.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    suspend fun add(task: Task): Long = taskDao.insert(task.toEntity())

    fun get(): Flow<List<Task>> =
        taskDao.getTasksOrderByCreatedAt().map { entities ->
            entities.map { it.toModel() }
        }

    fun getById(id: Long): Flow<Task?> =
        taskDao.getTaskById(id).map { it?.toModel() }
}