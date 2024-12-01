package jp.ntsk.room.schema.docs.sample.database

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.ntsk.room.schema.docs.sample.entity.TaskEntity

private const val APP_DB_VERSION = 1

@Database(
    version = APP_DB_VERSION,
    entities = [
        TaskEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}
