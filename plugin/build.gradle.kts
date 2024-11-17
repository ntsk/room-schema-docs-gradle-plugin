plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.dsl)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.java.gradle.plugin)
    alias(libs.plugins.maven.publish)
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
    plugins {
        create("roomSchemaDocsPlugin") {
            id = "jp.ntsk.room-schema-docs-gradle-plugin"
            implementationClass = "jp.ntsk.room.schema.docs.plugin.RoomSchemaDocsPlugin"
        }
    }
    isAutomatedPublishing = false
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}

publishing {
    publications {
        create<MavenPublication>("pluginMaven") {
            from(components["java"])

            pom {
                name.set("room-schema-docs-gradle-plugin")
                description.set("A Gradle plugin for generating Room schema documentation.")
                url.set("https://github.com/ntsk/room-schema-docs-gradle-plugin")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                artifactId = "room-schema-docs-gradle-plugin"
            }
        }
    }
    repositories {
        mavenLocal()
    }
}