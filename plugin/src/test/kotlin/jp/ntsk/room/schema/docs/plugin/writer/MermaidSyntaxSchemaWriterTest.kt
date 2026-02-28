package jp.ntsk.room.schema.docs.plugin.writer

import jp.ntsk.room.schema.docs.plugin.entity.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContains
import kotlin.test.assertFalse

class MermaidSyntaxSchemaWriterTest {
    private val writer = MermaidSyntaxSchemaWriter()

    private fun createSchema(entities: List<Entity>): RoomSchema {
        return RoomSchema(
            formatVersion = 1,
            database = Database(
                version = 1,
                identityHash = "test",
                entities = entities,
                setupQueries = emptyList()
            )
        )
    }

    private fun createSimpleEntity(
        tableName: String,
        fields: List<Field>,
        primaryKeyColumns: List<String>,
        indices: List<Index> = emptyList(),
        foreignKeys: List<ForeignKey> = emptyList()
    ): Entity {
        return Entity(
            tableName = tableName,
            createSql = "",
            fields = fields,
            primaryKey = PrimaryKey(autoGenerate = true, columnNames = primaryKeyColumns),
            indices = indices,
            foreignKeys = foreignKeys
        )
    }

    @Test
    fun `write generates mermaid code block`() {
        val schema = createSchema(emptyList())
        val result = writer.write(schema)

        assert(result.startsWith("```mermaid\n"))
        assert(result.endsWith("```"))
        assertContains(result, "erDiagram")
    }

    @Test
    fun `write generates table with fields`() {
        val entity = createSimpleEntity(
            tableName = "users",
            fields = listOf(
                Field("id", "id", "INTEGER", true),
                Field("name", "name", "TEXT", true)
            ),
            primaryKeyColumns = listOf("id")
        )
        val result = writer.write(createSchema(listOf(entity)))

        assertContains(result, "users {")
        assertContains(result, "  id INTEGER PK")
        assertContains(result, "  name TEXT")
        assertFalse(result.contains("  name TEXT PK"))
    }

    @Test
    fun `write marks foreign key columns`() {
        val users = createSimpleEntity(
            tableName = "users",
            fields = listOf(Field("id", "id", "INTEGER", true)),
            primaryKeyColumns = listOf("id")
        )
        val tasks = createSimpleEntity(
            tableName = "tasks",
            fields = listOf(
                Field("id", "id", "INTEGER", true),
                Field("userId", "user_id", "INTEGER", true)
            ),
            primaryKeyColumns = listOf("id"),
            foreignKeys = listOf(
                ForeignKey("users", "CASCADE", "NO ACTION", listOf("user_id"), listOf("id"))
            )
        )
        val result = writer.write(createSchema(listOf(users, tasks)))

        assertContains(result, "  user_id INTEGER FK")
    }

    @Test
    fun `write generates has-many relation`() {
        val users = createSimpleEntity(
            tableName = "users",
            fields = listOf(Field("id", "id", "INTEGER", true)),
            primaryKeyColumns = listOf("id")
        )
        val tasks = createSimpleEntity(
            tableName = "tasks",
            fields = listOf(
                Field("id", "id", "INTEGER", true),
                Field("userId", "user_id", "INTEGER", true)
            ),
            primaryKeyColumns = listOf("id"),
            indices = listOf(
                Index("index_tasks_user_id", false, listOf("user_id"), emptyList(), "")
            ),
            foreignKeys = listOf(
                ForeignKey("users", "CASCADE", "NO ACTION", listOf("user_id"), listOf("id"))
            )
        )
        val result = writer.write(createSchema(listOf(users, tasks)))

        assertContains(result, "users ||--o{ tasks : \"FK(user_id)\"")
    }

    @Test
    fun `write generates has-one relation for unique non-nullable FK`() {
        val users = createSimpleEntity(
            tableName = "users",
            fields = listOf(Field("id", "id", "INTEGER", true)),
            primaryKeyColumns = listOf("id")
        )
        val profiles = createSimpleEntity(
            tableName = "profiles",
            fields = listOf(
                Field("id", "id", "INTEGER", true),
                Field("userId", "user_id", "INTEGER", true)
            ),
            primaryKeyColumns = listOf("id"),
            indices = listOf(
                Index("index_profiles_user_id", true, listOf("user_id"), emptyList(), "")
            ),
            foreignKeys = listOf(
                ForeignKey("users", "CASCADE", "NO ACTION", listOf("user_id"), listOf("id"))
            )
        )
        val result = writer.write(createSchema(listOf(users, profiles)))

        assertContains(result, "users ||--|| profiles : \"FK(user_id)\"")
    }

    @Test
    fun `write generates has-zero-or-one relation for unique nullable FK`() {
        val users = createSimpleEntity(
            tableName = "users",
            fields = listOf(Field("id", "id", "INTEGER", true)),
            primaryKeyColumns = listOf("id")
        )
        val subscriptions = createSimpleEntity(
            tableName = "subscriptions",
            fields = listOf(
                Field("id", "id", "INTEGER", true),
                Field("userId", "user_id", "INTEGER", false)
            ),
            primaryKeyColumns = listOf("id"),
            indices = listOf(
                Index("index_subscriptions_user_id", true, listOf("user_id"), emptyList(), "")
            ),
            foreignKeys = listOf(
                ForeignKey("users", "SET NULL", "NO ACTION", listOf("user_id"), listOf("id"))
            )
        )
        val result = writer.write(createSchema(listOf(users, subscriptions)))

        assertContains(result, "users ||--o| subscriptions : \"FK(user_id)\"")
    }

    @Test
    fun `write marks composite primary key`() {
        val entity = createSimpleEntity(
            tableName = "task_tags",
            fields = listOf(
                Field("taskId", "task_id", "INTEGER", true),
                Field("tagId", "tag_id", "INTEGER", true)
            ),
            primaryKeyColumns = listOf("task_id", "tag_id")
        )
        val result = writer.write(createSchema(listOf(entity)))

        assertContains(result, "  task_id INTEGER PK")
        assertContains(result, "  tag_id INTEGER PK")
    }

    @Test
    fun `write marks composite PK and FK`() {
        val tasks = createSimpleEntity(
            tableName = "tasks",
            fields = listOf(Field("id", "id", "INTEGER", true)),
            primaryKeyColumns = listOf("id")
        )
        val tags = createSimpleEntity(
            tableName = "tags",
            fields = listOf(Field("id", "id", "INTEGER", true)),
            primaryKeyColumns = listOf("id")
        )
        val crossRef = createSimpleEntity(
            tableName = "task_tags",
            fields = listOf(
                Field("taskId", "task_id", "INTEGER", true),
                Field("tagId", "tag_id", "INTEGER", true)
            ),
            primaryKeyColumns = listOf("task_id", "tag_id"),
            foreignKeys = listOf(
                ForeignKey("tasks", "CASCADE", "NO ACTION", listOf("task_id"), listOf("id")),
                ForeignKey("tags", "CASCADE", "NO ACTION", listOf("tag_id"), listOf("id"))
            )
        )
        val result = writer.write(createSchema(listOf(tasks, tags, crossRef)))

        assertContains(result, "  task_id INTEGER PK,FK")
        assertContains(result, "  tag_id INTEGER PK,FK")
    }

    @Test
    fun `write handles entity with no relations`() {
        val entity = createSimpleEntity(
            tableName = "standalone",
            fields = listOf(
                Field("id", "id", "INTEGER", true),
                Field("value", "value", "TEXT", true)
            ),
            primaryKeyColumns = listOf("id")
        )
        val result = writer.write(createSchema(listOf(entity)))

        assertContains(result, "standalone {")
        assertFalse(result.contains("||"))
    }
}
