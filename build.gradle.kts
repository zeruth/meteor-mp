plugins {
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    kotlin("jvm") version "2.3.0" apply false
}

group = "io.github.nullpops"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}