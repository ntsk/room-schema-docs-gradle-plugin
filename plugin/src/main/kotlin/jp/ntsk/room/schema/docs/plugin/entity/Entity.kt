package jp.ntsk.room.schema.docs.plugin.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Entity(
    @SerialName("tableName") val tableName: String,
    @SerialName("createSql") val createSql: String,
    @SerialName("fields") val fields: List<Field>,
    @SerialName("primaryKey") val primaryKey: PrimaryKey,
    @SerialName("indices") val indices: List<Index> = emptyList(),
    @SerialName("foreignKeys") val foreignKeys: List<ForeignKey> = emptyList()
)

