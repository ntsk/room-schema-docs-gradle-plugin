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
            id = "jp.ntsk.room-schema-docs"
            implementationClass = "jp.ntsk.room.schema.docs.plugin.RoomSchemaDocsPlugin"
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