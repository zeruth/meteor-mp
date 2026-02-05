import nulled.InjectTask

plugins {
    kotlin("jvm")
    id("java-library")
    id("nulled.injector")
}

group = "io.github.nullpops"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val injectTask = tasks.register<InjectTask>("injectMultiplatform") {
    outputs.upToDateWhen { false }

    dependsOn(":api-rs:build")
    dependsOn(":mixins:build")
    dependsOn(":rs:build")

    api = "${project.layout.projectDirectory}/../api-rs/build/classes/java/main/net/runelite/rs/api/"
    mixins = "${project.layout.projectDirectory}/../mixins/build/libs/mixins-$version.jar"
    target = "${project.layout.projectDirectory}/../rs/build/libs/rs-$version.jar"
    output = File("${project.layout.projectDirectory}/../lib/injected-client.jar")
}

tasks.jar {
    enabled = false
}
