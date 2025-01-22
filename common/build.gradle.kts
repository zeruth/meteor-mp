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
    compileOnly(files("../lib/android-35.jar"))

    with(compose) {
        compileOnly(runtime)
        compileOnly(ui)
        compileOnly(desktop.currentOs)
    }

    with(libs) {
        compileOnly(kotlin.reflect)
        compileOnly(logger)
        compileOnly(eventbus)
        compileOnly(gson)
        compileOnly(material3)
        compileOnly(line.awesome)
    }
}