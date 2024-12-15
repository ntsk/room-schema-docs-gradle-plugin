package jp.ntsk.room.schema.docs.sample.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["author_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["assignee_id"],
            onDelete = ForeignKey.SET_NULL
        ),
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "status")
    val status: Int,

    @ColumnInfo(name = "author_id")
    val authorId: Long?,

    @ColumnInfo(name = "assignee_id")
    val assigneeId: Long?,

    @ColumnInfo(name = "due_at")
    val dueAt: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
)

