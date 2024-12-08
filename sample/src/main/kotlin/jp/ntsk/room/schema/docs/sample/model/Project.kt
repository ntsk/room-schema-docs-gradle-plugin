package jp.ntsk.room.schema.docs.sample.model

import jp.ntsk.room.schema.docs.sample.entity.ProjectEntity
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class Project(
    val id: Long,
    val title: String,
    val createdAt: OffsetDateTime,
)

fun ProjectEntity.toModel() = Project(
    id = this.id,
    title = this.title,
    createdAt = OffsetDateTime.ofInstant(
        Instant.ofEpochSecond(this.createdAt),
        ZoneOffset.UTC
    )
)
