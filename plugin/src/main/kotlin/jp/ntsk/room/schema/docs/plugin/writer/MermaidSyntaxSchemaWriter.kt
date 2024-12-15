package jp.ntsk.room.schema.docs.plugin.writer

import jp.ntsk.room.schema.docs.plugin.entity.RoomSchema

class MermaidSyntaxSchemaWriter : SchemaWriter {
    override fun write(schema: RoomSchema): String {
        // Generate table definitions
        val tables = schema.database.entities.joinToString("\n\n") { entity ->
            val primaryKeys = entity.primaryKey.columnNames.toSet()
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

                val isUnique = entity.indices.any { index ->
                    index.unique && index.columnNames.containsAll(foreignKey.columns)
                }
                val isNullable = foreignKey.columns.any { column ->
                    val field = entity.fields.find { it.columnName == column }
                    field?.notNull == false
                }
                val relationSyntax = when {
                    isUnique && !isNullable -> HAS_ONE
                    isUnique && isNullable -> HAS_ZERO_OR_ONE
                    else -> HAS_MANY
                }
                "${foreignKey.table} $relationSyntax ${entity.tableName} : \"$relationLabel\""
            }
        }.joinToString("\n")

        return buildString {
            append(CODE_SYNTAX_START)
            append("\n")
            append(ER_DIAGRAM)

            if (tables.isNotEmpty()) {
                append("\n")
                append(tables)
                append("\n")
            }
            if (relations.isNotEmpty()) {
                append("\n")
                append(relations)
                append("\n")
            }

            append(CODE_SYNTAX_END)
        }
    }

    companion object {
        private const val CODE_SYNTAX_START = "```mermaid"
        private const val CODE_SYNTAX_END = "```"
        private const val ER_DIAGRAM = "erDiagram"
        private const val HAS_MANY = "||--o{"
        private const val HAS_ONE = "||--||"
        private const val HAS_ZERO_OR_ONE = "||--o|"
    }
}