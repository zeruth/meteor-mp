plugins {
    kotlin("jvm")
    id("application")
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

application {
    mainClass.set("desktop.Main")
}

tasks.configureEach {
    if (name.contains("startScripts") || name == "run") {
        dependsOn(":injected-client:injectMultiplatform")
    }
}

