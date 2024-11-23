package jp.ntsk.room.schema.docs.plugin.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomSchema(
    @SerialName("formatVersion") val formatVersion: Int,
    @SerialName("database") val database: Database
)

