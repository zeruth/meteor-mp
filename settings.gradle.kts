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

rootProject.name = "Meteor Android"
include(":android")
include(":desktop")
include(":android-awt")
include(":rs2")
include(":common")
