pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://raw.githubusercontent.com/zeruth/repo/main/") }
        maven { url = uri("https://raw.githubusercontent.com/MeteorLite/hosting/main/repo/") }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("de.fayard.refreshVersions") version "0.60.6"
}

rootProject.name = "meteor"

includeBuild("injector")

include("api")
include("api-rs")
include("common")
include("client-android")
include("client-common")
include("client-desktop")
include("mixins")
include("rs")
include("injected-client")