package jp.ntsk.room.schema.docs.plugin.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Database(
    @SerialName("version") val version: Int,
    @SerialName("identityHash") val identityHash: String,
    @SerialName("entities") val entities: List<Entity>,
    @SerialName("setupQueries") val setupQueries: List<String>
)

