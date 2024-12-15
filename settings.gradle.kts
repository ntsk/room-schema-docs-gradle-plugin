pluginManagement {
    repositories {
        if (System.getenv("PLUGIN_ONLY")?.toBoolean() != true) {
            mavenLocal()
        }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        if (System.getenv("PLUGIN_ONLY")?.toBoolean() != true) {
            mavenLocal()
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "room-schema-docs-gradle-plugin"
if (System.getenv("PLUGIN_ONLY")?.toBoolean() != true) {
    include(":sample")
}
include(":plugin")
