package jp.ntsk.room.schema.docs.sample.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index

@Entity(
    primaryKeys = ["task_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["tag_id"]),
        Index(value = ["task_id"]),
    ]
)
data class TaskTagCrossRef(
    @ColumnInfo(name = "task_id")
    val taskId: Long,

    @ColumnInfo(name = "tag_id")
    val tagId: Long
)
