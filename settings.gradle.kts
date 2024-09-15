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
        maven { url = uri("https://raw.githubusercontent.com/zeruth/repo/main/") }
        maven { url = uri("https://raw.githubusercontent.com/MeteorLite/hosting/main/repo/") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven { url = uri("https://raw.githubusercontent.com/MeteorLite/hosting/main/repo/") }
        maven { url = uri("https://raw.githubusercontent.com/zeruth/repo/main/") }
    }
}

rootProject.name = "Meteor Android"
include(":app")
include(":native-awt")
include(":deob")
include(":api-rs")
include(":mixins")
include(":api")
