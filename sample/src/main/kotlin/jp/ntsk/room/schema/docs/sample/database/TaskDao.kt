package jp.ntsk.room.schema.docs.sample.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import jp.ntsk.room.schema.docs.sample.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
@Singleton
interface TaskDao {
    @Upsert
    suspend fun insert(taskEntity: TaskEntity): Long

    @Query("SELECT * FROM tasks ORDER BY created_at DESC")
    fun getTasksOrderByCreatedAt(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Long): Flow<TaskEntity?>
}