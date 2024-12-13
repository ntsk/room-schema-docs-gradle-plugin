package jp.ntsk.room.schema.docs.sample.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import jp.ntsk.room.schema.docs.sample.entity.SubTaskEntity
import javax.inject.Singleton

@Dao
@Singleton
interface SubTaskDao {
    @Upsert
    suspend fun insert(subTaskEntity: SubTaskEntity): Long

    @Delete
    suspend fun delete(subTaskEntity: SubTaskEntity)
}