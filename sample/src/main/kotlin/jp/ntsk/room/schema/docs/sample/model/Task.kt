package jp.ntsk.room.schema.docs.sample.model

import androidx.compose.ui.graphics.Color
import jp.ntsk.room.schema.docs.sample.entity.TaskEntity
import jp.ntsk.room.schema.docs.sample.model.Task.Status.Companion.findStatusById
import jp.ntsk.room.schema.docs.sample.theme.Green40
import jp.ntsk.room.schema.docs.sample.theme.Purple40
import jp.ntsk.room.schema.docs.sample.theme.Red40
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val status: Status,
    val project: Project?,
    val dueAt: OffsetDateTime,
    val createdAt: OffsetDateTime,
) {
    enum class Status(val id: Int, val label: String, val color: Color) {
        None(0, "None", Color.White),
        Todo(1, "Todo", Color.LightGray),
        InProgress(2, "In Progress", Green40),
        Done(3, "Done", Purple40),
        Archived(4, "Archived", Red40);

        companion object {
            fun findStatusById(id: Int): Status {
                return Status.entries.find { id == it.id } ?: None
            }
        }
    }
}

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = this.title,
    description = this.description,
    status = this.status.id,
    createdAt = this.createdAt.toEpochSecond(),
    dueAt = this.dueAt.toEpochSecond()
)

fun TaskEntity.toModel(
    project: Project? = null
): Task = Task(
    id = this.id,
    title = this.title,
    description = this.description,
    status = findStatusById(this.status),
    project = project,
    createdAt = OffsetDateTime.ofInstant(
        Instant.ofEpochSecond(this.createdAt),
        ZoneOffset.UTC
    ),
    dueAt = OffsetDateTime.ofInstant(
        Instant.ofEpochSecond(this.dueAt), ZoneOffset.UTC
    ),
)
