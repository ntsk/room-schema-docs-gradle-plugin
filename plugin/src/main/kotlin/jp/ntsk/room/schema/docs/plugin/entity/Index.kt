package jp.ntsk.room.schema.docs.plugin.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Index(
    @SerialName("name") val name: String,
    @SerialName("unique") val unique: Boolean,
    @SerialName("columnNames") val columnNames: List<String>,
    @SerialName("orders") val orders: List<String> = emptyList(),
    @SerialName("createSql") val createSql: String
)

