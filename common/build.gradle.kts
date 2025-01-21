plugins {
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(compose.runtime)
    compileOnly(compose.ui)
    compileOnly(compose.desktop.currentOs)
    compileOnly(libs.logger)
    compileOnly(libs.eventbus)
    compileOnly(libs.gson)
    compileOnly(libs.material3)
    compileOnly(libs.line.awesome)
    compileOnly(files("../lib/android-35.jar"))
}