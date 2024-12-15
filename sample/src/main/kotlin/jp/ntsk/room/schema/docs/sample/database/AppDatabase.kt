package jp.ntsk.room.schema.docs.sample.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import jp.ntsk.room.schema.docs.sample.entity.SubTaskEntity
import jp.ntsk.room.schema.docs.sample.entity.TaskEntity
import jp.ntsk.room.schema.docs.sample.entity.UserEntity

private const val APP_DB_VERSION = 4

@Database(
    version = APP_DB_VERSION,
    entities = [
        TaskEntity::class,
        SubTaskEntity::class,
        UserEntity::class,
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = AutoMigrationSpec2To3::class),
        AutoMigration(from = 3, to = 4),
    ]
)
abstract class AppDatabase : RoomDatabase()

@DeleteTable(tableName = "projects")
@DeleteColumn.Entries(
    DeleteColumn(
        tableName = "tasks",
        columnName = "project_id"
    )
)
class AutoMigrationSpec2To3 : AutoMigrationSpec
