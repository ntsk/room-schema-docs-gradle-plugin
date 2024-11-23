package jp.ntsk.room.schema.docs.plugin

import jp.ntsk.room.schema.docs.plugin.serializer.SchemaSerializer
import jp.ntsk.room.schema.docs.plugin.writer.MermaidSyntaxSchemaWriter
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class RoomSchemaDocsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.create("roomSchemaDocs", RoomSchemaDocsExtension::class.java)

        project.tasks.register("generateRoomSchemaDocs") {
            group = "documentation"
            description = "Generate Mermaid ER diagrams from Room schema JSON files."

            doLast {
                val schemaDir = File(extension.schemaDir)
                val outputDir = File(extension.outputDir)
                val files = schemaDir.listFiles()

                if (!schemaDir.exists()) {
                    throw IllegalArgumentException("Schema directory does not exist: ${extension.schemaDir}")
                }

                if (files.isNullOrEmpty()) {
                    throw IllegalArgumentException("Schema files does not exist: ${extension.schemaDir}")
                }

                if (!outputDir.exists()) {
                    outputDir.mkdirs()
                }

                val serializer = SchemaSerializer()
                val writer = MermaidSyntaxSchemaWriter()

                files
                    .filter { file -> file.extension == "json" }
                    .forEach { file ->
                        val roomSchema = serializer.serialize(file)
                        val erDiagram = writer.write(roomSchema)
                        val output = File(outputDir, "${roomSchema.database.version}.md")
                        output.writeText(erDiagram)
                        println("Generated ER diagram: ${output.absolutePath}")
                    }
            }
        }
    }
}

open class RoomSchemaDocsExtension {
    var schemaDir: String = "schemas"
    var outputDir: String = "docs"
}