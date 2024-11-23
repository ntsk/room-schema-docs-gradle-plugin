package jp.ntsk.room.schema.docs.plugin.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrimaryKey(
    @SerialName("autoGenerate") val autoGenerate: Boolean,
    @SerialName("columnNames") val columnNames: List<String>
)
