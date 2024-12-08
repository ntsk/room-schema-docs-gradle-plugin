# Room Schema Docs Gradle Plugin

A Gradle plugin to automatically generate Entity-Relationship (ER) diagrams in Mermaid format from Android Room database schema JSON files.

## Features

- Converts Room schema JSON files into ER diagrams in [Mermaid](https://mermaid.js.org/syntax/entityRelationshipDiagram.html#entity-relationship-diagrams) syntax.
- Outputs diagrams embedded in Markdown files, making it easy to review schema relationships and manage database changes over time.

## Getting Started

### Prerequisites

- Android Room configured to export schemas.
- Gradle 7.x or higher.

### Installation

1. Add the plugin to your `build.gradle.kts`:
```kotlin
plugins {
    id("jp.ntsk.room-schema-docs") version "1.0.0"
}
```

2. Ensure Room's schema export is enabled by adding the following to your `build.gradle.kts`:
```kotlin
room {
    schemaDirectory("$projectDir/schemas")
}
```

3. Configure the plugin in your `build.gradle.kts`:
```kotlin
roomSchemaDocs {
    schemaDir = "$projectDir/schemas"
    outputDir = "$projectDir/schemas-docs"
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
