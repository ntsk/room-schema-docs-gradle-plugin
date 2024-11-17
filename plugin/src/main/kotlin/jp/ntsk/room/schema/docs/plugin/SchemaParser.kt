package jp.ntsk.room.schema.docs.plugin

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class RoomSchema(
    @SerialName("formatVersion") val formatVersion: Int,
    @SerialName("database") val database: Database
)

@Serializable
data class Database(
    @SerialName("version") val version: Int,
    @SerialName("identityHash") val identityHash: String,
    @SerialName("entities") val entities: List<Entity>,
    @SerialName("setupQueries") val setupQueries: List<String>
)

@Serializable
data class Entity(
    @SerialName("tableName") val tableName: String,
    @SerialName("createSql") val createSql: String,
    @SerialName("fields") val fields: List<Field>,
    @SerialName("primaryKey") val primaryKey: PrimaryKey,
    @SerialName("indices") val indices: List<Index> = emptyList(),
    @SerialName("foreignKeys") val foreignKeys: List<ForeignKey> = emptyList()
)

@Serializable
data class Field(
    @SerialName("fieldPath") val fieldPath: String,
    @SerialName("columnName") val columnName: String,
    @SerialName("affinity") val affinity: String,
    @SerialName("notNull") val notNull: Boolean
)

@Serializable
data class PrimaryKey(
    @SerialName("autoGenerate") val autoGenerate: Boolean,
    @SerialName("columnNames") val columnNames: List<String>
)

@Serializable
data class Index(
    @SerialName("name") val name: String,
    @SerialName("unique") val unique: Boolean,
    @SerialName("columnNames") val columnNames: List<String>,
    @SerialName("orders") val orders: List<String> = emptyList(),
    @SerialName("createSql") val createSql: String
)

@Serializable
data class ForeignKey(
    @SerialName("table") val table: String,
    @SerialName("onDelete") val onDelete: String,
    @SerialName("onUpdate") val onUpdate: String,
    @SerialName("columns") val columns: List<String>,
    @SerialName("referencedColumns") val referencedColumns: List<String>
)

class SchemaParser(private val file: File) {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    val dbVersion: Int

    init {
        val schema = json.decodeFromString<RoomSchema>(file.readText())
        dbVersion = schema.database.version
    }

    fun generateMermaidSyntax(): String {
        val schema = json.decodeFromString<RoomSchema>(file.readText())

        // Generate table definitions
        val tables = schema.database.entities.joinToString("\n\n") { entity ->
            val primaryKeys = entity.primaryKey?.columnNames?.toSet() ?: emptySet()
            val foreignKeyColumns = entity.foreignKeys.flatMap { it.columns }.toSet()

            val fields = entity.fields.joinToString("\n") { field ->
                val constraints = mutableListOf<String>()
                if (field.columnName in primaryKeys) constraints.add("PK")
                if (field.columnName in foreignKeyColumns) constraints.add("FK")
                "  ${field.columnName} ${field.affinity} ${constraints.joinToString(",")}".trimEnd()
            }
            "${entity.tableName} {\n$fields\n}"
        }

        // Generate relations
        val relations = schema.database.entities.flatMap { entity ->
            entity.foreignKeys.map { foreignKey ->
                val relationLabel =
                    foreignKey.columns.joinToString(", ") { column -> "FK($column)" }
                "${entity.tableName} ||--o{ ${foreignKey.table} : \"$relationLabel\""
            }
        }.joinToString("\n")

        return buildString {
            append(CODE_SYNTAX_START)
            append("\n")
            append(ER_DIAGRAM)
            append("\n")
            append(tables)
            append("\n\n")
            append(relations)
            append("\n")
            append(CODE_SYNTAX_END)
        }
    }

    companion object {
        private const val CODE_SYNTAX_START = "```mermaid"
        private const val CODE_SYNTAX_END = "```"
        private const val ER_DIAGRAM = "erDiagram"
    }
}

