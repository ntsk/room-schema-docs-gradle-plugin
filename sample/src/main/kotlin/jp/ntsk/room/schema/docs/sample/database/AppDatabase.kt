package jp.ntsk.room.schema.docs.sample.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import jp.ntsk.room.schema.docs.sample.entity.ProjectEntity
import jp.ntsk.room.schema.docs.sample.entity.TaskEntity
import jp.ntsk.room.schema.docs.sample.ui.Screens

private const val APP_DB_VERSION = 2

@Database(
    version = APP_DB_VERSION,
    entities = [
        TaskEntity::class,
        ProjectEntity::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val projectDao: ProjectDao
}
