package jp.ntsk.room.schema.docs.plugin.serializer

import jp.ntsk.room.schema.docs.plugin.entity.RoomSchema
import kotlinx.serialization.json.*
import java.io.File

class SchemaSerializer {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun serialize(file: File): RoomSchema {
        return json.decodeFromString<RoomSchema>(file.readText())
    }
}
