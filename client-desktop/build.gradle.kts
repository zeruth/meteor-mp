import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("jvm")
}

dependencies {
    implementation(libs.kotlin.reflect)

    implementation(project(":api"))
    implementation(project(":api-rs"))
    implementation(project(":common"))
    implementation(project(":client-common"))

    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(compose.desktop.currentOs)

    //Not ideal but it works...
    implementation(project(":injected-client"))
    implementation(files("../lib/injected-client.jar"))
}

compose.desktop {
    application {
        mainClass = "desktop.Main"
        version = rootProject.version as String

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "meteor"
            packageVersion = rootProject.version as String
            windows {

                console = true
                upgradeUuid = "9df19034-e962-4bb4-90c0-74330a07082b"
                iconFile.set(project.file("src/main/resources/Meteor.ico"))
                shortcut = true
            }
        }
    }
}

tasks.configureEach {
    if (name.contains("compile")) {
        dependsOn(":injected-client:injectMultiplatform")
    }
}

