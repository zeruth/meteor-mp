import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("jvm")
}

group = "meteor"
version = "2.1.1-SNAPSHOT"

dependencies {
    implementation(project(":common"))
    implementation(project(":rs2"))

    with(compose) {
        implementation(runtime)
        implementation(ui)
        implementation(desktop.currentOs)
    }

    with(libs) {
        implementation(eventbus)
        implementation(logger)
        implementation(kotlin.reflect)
        implementation(line.awesome)
        implementation(libs.material3)
        implementation(gson)
        implementation(kpresence)
    }
}

kotlin {
    jvmToolchain(21)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21) // Specify the desired Java version here
    }
}


compose.desktop {
    application {
        jvmArgs("-Dsun.java2d.uiScale=1.0")
        mainClass = "meteor.Main"
        version = "2.1.1"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "meteor"
            packageVersion = "2.1.1"
            windows {

                console = true
                upgradeUuid = "9df19034-e962-4bb4-90c0-74330a07082b"
                iconFile.set(project.file("src/main/resources/Meteor.ico"))
                shortcut = true
            }
        }
    }
}