package jp.ntsk.room.schema.docs.sample.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import jp.ntsk.room.schema.docs.sample.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
@Singleton
interface ProjectDao {
    @Upsert
    suspend fun insert(projectEntity: ProjectEntity): Long

    @Delete
    suspend fun delete(projectEntity: ProjectEntity)

    @Query("SELECT * FROM tasks ORDER BY created_at DESC")
    fun getProjectsOrderByCreatedAt(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getProjectById(id: Long): Flow<ProjectEntity?>
}