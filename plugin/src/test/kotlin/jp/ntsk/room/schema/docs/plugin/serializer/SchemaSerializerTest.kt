package jp.ntsk.room.schema.docs.plugin.serializer

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SchemaSerializerTest {
    private val serializer = SchemaSerializer()

    private fun loadResource(name: String): File {
        return File(javaClass.classLoader.getResource("schemas/$name")!!.toURI())
    }

    @Test
    fun `serialize parses single entity schema`() {
        val schema = serializer.serialize(loadResource("single_entity.json"))

        assertEquals(1, schema.formatVersion)
        assertEquals(1, schema.database.version)
        assertEquals(1, schema.database.entities.size)

        val entity = schema.database.entities[0]
        assertEquals("users", entity.tableName)
        assertEquals(2, entity.fields.size)
        assertEquals("id", entity.fields[0].columnName)
        assertEquals("INTEGER", entity.fields[0].affinity)
        assertTrue(entity.fields[0].notNull)
        assertEquals("name", entity.fields[1].columnName)
        assertEquals("TEXT", entity.fields[1].affinity)
    }

    @Test
    fun `serialize parses primary key`() {
        val schema = serializer.serialize(loadResource("single_entity.json"))
        val pk = schema.database.entities[0].primaryKey

        assertTrue(pk.autoGenerate)
        assertEquals(listOf("id"), pk.columnNames)
    }

    @Test
    fun `serialize parses foreign keys`() {
        val schema = serializer.serialize(loadResource("with_relations.json"))
        val tasks = schema.database.entities.find { it.tableName == "tasks" }!!

        assertEquals(1, tasks.foreignKeys.size)
        val fk = tasks.foreignKeys[0]
        assertEquals("users", fk.table)
        assertEquals(listOf("user_id"), fk.columns)
        assertEquals(listOf("id"), fk.referencedColumns)
        assertEquals("CASCADE", fk.onDelete)
        assertEquals("NO ACTION", fk.onUpdate)
    }

    @Test
    fun `serialize parses indices`() {
        val schema = serializer.serialize(loadResource("with_relations.json"))
        val profiles = schema.database.entities.find { it.tableName == "profiles" }!!

        assertEquals(1, profiles.indices.size)
        val index = profiles.indices[0]
        assertTrue(index.unique)
        assertEquals(listOf("user_id"), index.columnNames)
    }

    @Test
    fun `serialize throws on invalid json`() {
        val tempFile = File.createTempFile("invalid", ".json").apply {
            writeText("not valid json")
            deleteOnExit()
        }
        assertFailsWith<Exception> {
            serializer.serialize(tempFile)
        }
    }
}
