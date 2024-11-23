package jp.ntsk.room.schema.docs.plugin.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForeignKey(
    @SerialName("table") val table: String,
    @SerialName("onDelete") val onDelete: String,
    @SerialName("onUpdate") val onUpdate: String,
    @SerialName("columns") val columns: List<String>,
    @SerialName("referencedColumns") val referencedColumns: List<String>
)

