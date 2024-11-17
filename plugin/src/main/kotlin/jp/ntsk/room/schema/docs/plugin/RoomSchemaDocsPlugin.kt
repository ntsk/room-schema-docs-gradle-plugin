package jp.ntsk.room.schema.docs.plugin

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

                if (!schemaDir.exists()) {
                    throw IllegalArgumentException("Schema directory does not exist: ${extension.schemaDir}")
                }
                if (!outputDir.exists()) {
                    outputDir.mkdirs()
                }

                schemaDir.listFiles()?.forEach { file ->
                    if (file.extension == "json") {
                        val parser = SchemaParser(file)
                        val erDiagram = parser.generateMermaidSyntax()
                        val output = File(outputDir, "${parser.dbVersion}.md")
                        output.writeText(erDiagram)
                        println("Generated ER diagram: ${output.absolutePath}")
                    }
                }
            }
        }
    }
}

open class RoomSchemaDocsExtension {
    var schemaDir: String = "schemas"
    var outputDir: String = "docs"
}
