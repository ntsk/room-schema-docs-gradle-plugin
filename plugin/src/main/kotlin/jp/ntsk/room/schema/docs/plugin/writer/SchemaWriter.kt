package jp.ntsk.room.schema.docs.plugin.writer

import jp.ntsk.room.schema.docs.plugin.entity.RoomSchema

interface SchemaWriter {
    fun write(schema: RoomSchema): String
}