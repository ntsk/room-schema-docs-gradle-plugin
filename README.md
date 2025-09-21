# Room Schema Docs Gradle Plugin
![build](https://github.com/ntsk/room-schema-docs-gradle-plugin/actions/workflows/build.yml/badge.svg)
[![Gradle Plugin Portal](https://img.shields.io/badge/gradle%20plugin-1.3.0-blue)](https://plugins.gradle.org/plugin/jp.ntsk.room-schema-docs)

A Gradle plugin to automatically generate Entity-Relationship (ER) diagrams in Mermaid format from Android Room database schema JSON files.

## Features
- Generates ER diagrams in [Mermaid](https://mermaid.js.org/syntax/entityRelationshipDiagram.html#entity-relationship-diagrams) syntax based on Room schema JSON files.
- Outputs diagrams embedded in Markdown files, making it easy to review schema relationships and manage database changes over time.

## Output Example
When schema JSON files like [sample/schemas](https://github.com/ntsk/room-schema-docs-gradle-plugin/tree/main/sample/schemas/jp.ntsk.room.schema.docs.sample.database.AppDatabase) exist, you can generate Markdown(Mermaid Syntax) documents like [sample/schemas-docs](https://github.com/ntsk/room-schema-docs-gradle-plugin/tree/main/sample/schemas-docs/jp.ntsk.room.schema.docs.sample.database.AppDatabase).


## Getting Started

### Prerequisites

- Android Room configured to export schemas.
- Gradle 7.x or higher.

### Installation

1. Add the plugin to your `build.gradle.kts`:
```kotlin
plugins {
    id("jp.ntsk.room-schema-docs") version "1.3.0"
}
```

2. Ensure Room's schema export is enabled by adding the following to your `build.gradle.kts`:
```kotlin
room {
    schemaDirectory("$projectDir/schemas")
}
```

refs. https://developer.android.com/training/data-storage/room/migrating-db-versions#set_schema_location_using_room_gradle_plugin

3. Configure the plugin in your `build.gradle.kts`:
```kotlin
roomSchemaDocs {
    schemaDirectory("$projectDir/schemas")
    outputDirectory("$projectDir/schemas-docs")
}
```

### Generating ER Diagrams

After setting up, run the following command to generate ER diagrams:

```bash
./gradlew generateRoomSchemaDocs
```

#### Output Example

For a directory structure like:
```
schemas/
├── <YOUR_PACKAGE_NAME>/
│   └── 1.json
```

The plugin will generate:
```
schemas-docs/
├── <YOUR_PACKAGE_NAME>/
│   └── 1.md
```
