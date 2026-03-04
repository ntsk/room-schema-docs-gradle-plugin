plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version "2.3.0"
    `maven-publish`
    id("com.gradle.plugin-publish") version "2.1.0"
    signing
    id("com.gradleup.shadow") version "9.3.2"
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
version = "1.4.0"

gradlePlugin {
    website.set("https://github.com/ntsk/room-schema-docs-gradle-plugin")
    vcsUrl.set("https://github.com/ntsk/room-schema-docs-gradle-plugin")
    plugins {
        create("roomSchemaDocsPlugin") {
            id = "jp.ntsk.room-schema-docs"
            implementationClass = "jp.ntsk.room.schema.docs.plugin.RoomSchemaDocsPlugin"
            displayName = "room-schema-docs-gradle-plugin"
            description =
                "A Gradle plugin to automatically generate Entity-Relationship (ER) diagrams from Android Room database schema JSON files in Mermaid format."
            tags = listOf("android", "androidx-room")
        }
    }
}

tasks.jar {
    archiveClassifier.set("plain")
}

tasks.shadowJar {
    relocate("kotlinx.serialization", "jp.ntsk.room.schema.docs.plugin.relocated.kotlinx.serialization")
    archiveClassifier.set("")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
    testImplementation(kotlin("test"))
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
