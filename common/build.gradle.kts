plugins {
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("jvm")
}

version = "2.1.3-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

dependencies {

    compileOnly(files("../lib/android-35.jar"))

    compileOnly(project(":rs2"))

    with(compose) {
        compileOnly(runtime)
        compileOnly(ui)
        compileOnly(desktop.currentOs)
    }

    with(libs) {
        implementation("org.seleniumhq.selenium:selenium-java:4.28.1")
        implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.28.1")
        implementation(libs.jsoup)
        implementation(libs.androidx.annotation.jvm)
        compileOnly(kotlin.reflect)
        compileOnly(logger)
        compileOnly(eventbus)
        compileOnly(gson)
        compileOnly(material3)
        compileOnly(line.awesome)
    }
}