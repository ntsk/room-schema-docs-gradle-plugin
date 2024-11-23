package jp.ntsk.room.schema.docs.plugin.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Field(
    @SerialName("fieldPath") val fieldPath: String,
    @SerialName("columnName") val columnName: String,
    @SerialName("affinity") val affinity: String,
    @SerialName("notNull") val notNull: Boolean
)
