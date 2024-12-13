package jp.ntsk.room.schema.docs.sample.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Upsert
import jp.ntsk.room.schema.docs.sample.entity.SubTaskEntity
import jp.ntsk.room.schema.docs.sample.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
@Singleton
interface TaskDao {
    @Upsert
    suspend fun insert(taskEntity: TaskEntity): Long

    @Delete
    suspend fun delete(taskEntity: TaskEntity)

    @Query("SELECT * FROM tasks ORDER BY created_at DESC")
    fun getTasksOrderByCreatedAt(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Long): Flow<TaskWithSubTasksEntity>
}

data class TaskWithSubTasksEntity(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val subTask: List<SubTaskEntity>
)