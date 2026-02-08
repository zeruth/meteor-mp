plugins {
    kotlin("jvm")
    id("application")
}

group = "io.github.nullpops"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("desktop.Main")
}

tasks.configureEach {
    if (name.contains("startScripts") || name == "run") {
        dependsOn(":injected-client:injectMultiplatform")
    }
}

dependencies {
    implementation(libs.kotlin.reflect)

    implementation(project(":api"))
    implementation(project(":api-rs"))
    implementation(project(":common"))
    implementation(project(":client-common"))

    //Not ideal but it works...
    implementation(project(":injected-client"))
    implementation(files("../lib/injected-client.jar"))
}

