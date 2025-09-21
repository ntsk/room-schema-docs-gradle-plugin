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
                val schemaDir = File(extension.resolveSchemaDir())
                val outputDir = File(extension.resolveOutputDir())

                if (!schemaDir.exists()) {
                    throw IllegalArgumentException("Schema directory does not exist: ${extension.resolveSchemaDir()}")
                }

                if (!outputDir.exists()) {
                    outputDir.mkdirs()
                }

                val serializer = SchemaSerializer()
                val writer = MermaidSyntaxSchemaWriter()

                val jsonFiles = schemaDir.walkTopDown()
                    .filter { it.isFile && it.extension == "json" }
                    .toList()

                if (jsonFiles.isEmpty()) {
                    throw IllegalArgumentException("No schema files found in directory or subdirectories: ${extension.resolveSchemaDir()}")
                }

                jsonFiles
                    .forEach { file ->
                        println(file.toPath())

                        val relativePath = schemaDir.toPath().relativize(file.toPath()).toString()
                        val outputFile = File(outputDir, relativePath.replace(".json", ".md"))
                        outputFile.parentFile.mkdirs()

                        val roomSchema = serializer.serialize(file)
                        val erDiagram = writer.write(roomSchema)

                        outputFile.writeText(erDiagram)
                        println("Generated ER diagram: ${outputFile.absolutePath}")
                    }
            }
        }
    }
}

open class RoomSchemaDocsExtension {
    @Deprecated("Use schemaDirectory() method instead", ReplaceWith("schemaDirectory(value)"))
    var schemaDir: String = "schemas"

    @Deprecated("Use outputDirectory() method instead", ReplaceWith("outputDirectory(value)"))
    var outputDir: String = "docs"

    private var schemaDirectoryValue: String? = null
    private var outputDirectoryValue: String? = null

    fun schemaDirectory(path: String) {
        schemaDirectoryValue = path
    }

    fun outputDirectory(path: String) {
        outputDirectoryValue = path
    }

    internal fun resolveSchemaDir(): String {
        return schemaDirectoryValue ?: schemaDir
    }

    internal fun resolveOutputDir(): String {
        return outputDirectoryValue ?: outputDir
    }
}