pluginManagement {
    repositories {
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
        google()
        mavenCentral()
    }
}

rootProject.name = "PIM-KawAPP"
include(":app")
include(":core:model")
include(":core:database")
include(":core:network")
include(":core:data")
include(":core:ui")
include(":feature:feed")
include(":feature:recipes")
include(":feature:profile")
include(":feature:journal")
