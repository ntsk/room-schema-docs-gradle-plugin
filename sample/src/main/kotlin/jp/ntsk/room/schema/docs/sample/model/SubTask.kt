package jp.ntsk.room.schema.docs.sample.model

import jp.ntsk.room.schema.docs.sample.entity.SubTaskEntity
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class SubTask(
    val id: Long,
    val title: String,
    val createdAt: OffsetDateTime,
)

fun SubTaskEntity.toModel() = SubTask(
    id = this.id,
    title = this.title,
    createdAt = OffsetDateTime.ofInstant(
        Instant.ofEpochSecond(this.createdAt),
        ZoneOffset.UTC
    )
)
