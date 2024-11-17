package jp.ntsk.room.schema.docs.plugin

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class RoomSchema(
    val database: String,
    val version: Int,
    val tables: List<Table>,
    val relations: List<Relation>
)

@Serializable
data class Table(
    val tableName: String,
    val columns: List<Column>
)

@Serializable
data class Column(
    val columnName: String,
    val type: String,
    val isPrimaryKey: Boolean
)

@Serializable
data class Relation(
    val fromTable: String,
    val toTable: String,
    val relationType: String
)

class SchemaParser(private val file: File) {
    val dbVersion: Int

    init {
        val schema = Json.decodeFromString<RoomSchema>(file.readText())
        dbVersion = schema.version
    }

    fun generateMermaidSyntax(): String {
        val schema = Json.decodeFromString<RoomSchema>(file.readText())
        val entities = schema.tables.joinToString("\n") {
            "class ${it.tableName} {\n" + it.columns.joinToString("\n") { column ->
                "  ${column.columnName}: ${column.type} ${if (column.isPrimaryKey) "(PK)" else ""}"
            } + "\n}"
        }
        val relations = schema.relations.joinToString("\n") {
            "${it.fromTable} -->|${it.relationType}| ${it.toTable}"
        }
        return """
        ```mermaid
        classDiagram
        $entities
        $relations
        ```
        """
    }
}
