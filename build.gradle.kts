plugins {
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    kotlin("jvm") version "2.3.0" apply false
    id("org.jetbrains.kotlin.android") version "2.3.0" apply false
    id("com.android.application") version "8.12.3" apply false
}

group = "io.github.nullpops"
version = "3.0.0"

subprojects {
    group = rootProject.group
    version = rootProject.version
}