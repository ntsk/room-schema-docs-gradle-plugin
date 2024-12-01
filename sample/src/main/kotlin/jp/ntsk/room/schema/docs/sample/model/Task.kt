package jp.ntsk.room.schema.docs.sample.model

import androidx.compose.ui.graphics.Color
import java.time.OffsetDateTime

data class Task(
    val id: Long,
    val name: String,
    val description: String,
    val status: Status,
    val createdAt: OffsetDateTime,
    val dueAt: OffsetDateTime,
) {
    enum class Status(val id: Int, val label: String, val color: Color) {
        TODO(0, "Todo", Color.LightGray),
        IN_PROGRESS(1, "In Progress", Color.Cyan),
        DONE(2, "Done", Color.Green),
        ARCHIVED(3, "Archived", Color.Red)
    }
}
