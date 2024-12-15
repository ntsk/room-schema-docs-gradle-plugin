package jp.ntsk.room.schema.docs.sample.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import jp.ntsk.room.schema.docs.sample.entity.ProfileEntity
import jp.ntsk.room.schema.docs.sample.entity.SubTaskEntity
import jp.ntsk.room.schema.docs.sample.entity.SubscriptionEntity
import jp.ntsk.room.schema.docs.sample.entity.TagEntity
import jp.ntsk.room.schema.docs.sample.entity.TaskEntity
import jp.ntsk.room.schema.docs.sample.entity.TaskTagCrossRef
import jp.ntsk.room.schema.docs.sample.entity.UserEntity

private const val APP_DB_VERSION = 7

@Database(
    version = APP_DB_VERSION,
    entities = [
        TaskEntity::class,
        SubTaskEntity::class,
        UserEntity::class,
        ProfileEntity::class,
        SubscriptionEntity::class,
        TagEntity::class,
        TaskTagCrossRef::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = AutoMigrationSpec2To3::class),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
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
