plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.dsl)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.java.gradle.plugin)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.gradle.plugin.publish)
    alias(libs.plugins.signing)
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

group = "jp.ntsk"
version = "1.0.0"

gradlePlugin {
    @Suppress("UnstableApiUsage")
    website.set("https://github.com/ntsk/room-schema-docs-gradle-plugin")
    @Suppress("UnstableApiUsage")
    vcsUrl.set("https://github.com/ntsk/room-schema-docs-gradle-plugin")
    plugins {
        create("roomSchemaDocsPlugin") {
            id = "jp.ntsk.room-schema-docs"
            implementationClass = "jp.ntsk.room.schema.docs.plugin.RoomSchemaDocsPlugin"
            displayName = "room-schema-docs-gradle-plugin"
            description =
                "A Gradle plugin to automatically generate Entity-Relationship (ER) diagrams from Android Room database schema JSON files in Mermaid format."
            @Suppress("UnstableApiUsage")
            tags = listOf("android", "androidx-room")
        }
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}

publishing {
    repositories {
        mavenLocal()
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

tasks.withType<Sign>().configureEach {
    onlyIf("Skip when publishing to mavenLocal") {
        !gradle.startParameter.taskNames.any { it.contains("publishToMavenLocal") }
    }
}
