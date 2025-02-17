import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import nulled.InjectTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("jvm")
    id("nulled.injector")
}


group = "meteor"
version = "2.1.5-SNAPSHOT"

tasks.withType<InjectTask> {
    outputs.upToDateWhen {
        false
    }
    dependsOn(":api-rs:build")
    dependsOn(":mixins:build")
    dependsOn(":rs2:build")
    api = "${project.layout.projectDirectory}/../api-rs/build/classes/java/main/net/runelite/rs/api/"
    mixins = "${project.layout.projectDirectory}/../mixins/build/libs/mixins-$version.jar"
    target = "${project.layout.projectDirectory}/../rs2/build/libs/rs2-$version.jar"
    output = "${project.layout.projectDirectory}/lib/injected-client.jar"
}

tasks.withType<KotlinCompile> {
    dependsOn("inject")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":api"))
    implementation(project(":api-rs"))
    implementation(files("./lib/injected-client.jar"))

    with(compose) {
        implementation(runtime)
        implementation(ui)
        implementation(desktop.currentOs)
    }

    with(libs) {
        implementation(java.websocket)
        implementation(eventbus)
        implementation(logger)
        implementation(kotlin.reflect)
        implementation(line.awesome)
        implementation(material3)
        implementation(gson)
        implementation(kpresence)
        implementation(java.websocket)
        implementation(logback.classic)
        implementation(slf4j.api)
        implementation("org.ow2.asm:asm:9.7.1")
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
        mainClass = "meteor.platform.desktop.Main"
        version = "2.1.5"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "meteor"
            packageVersion = "2.1.5"
            windows {

                console = true
                upgradeUuid = "9df19034-e962-4bb4-90c0-74330a07082b"
                iconFile.set(project.file("src/main/resources/Meteor.ico"))
                shortcut = true
            }
        }
    }
}