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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "My Application"
include(":app")
include(":core:accessibility")
include(":core:database")
include(":core:service")
include(":core:model")
include(":feature:interceptor:impl")
include(":feature:interceptor:api")
include(":feature:analytics:api")
include(":feature:analytics:impl")
include(":feature:settings:api")
include(":feature:settings:impl")
include(":feature:home:api")
include(":feature:home:impl")
