import nulled.InjectTask
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("nulled.injector")
}

group = "io.github.nullpops"
version = "1.0-SNAPSHOT"

tasks.withType<InjectTask> {
    outputs.upToDateWhen {
        false
    }
    dependsOn(":api-rs:build")
    dependsOn(":mixins:build")
    dependsOn(":rs:build")
    api = "${project.layout.projectDirectory}/../api-rs/build/classes/java/main/net/runelite/rs/api/"
    mixins = "${project.layout.projectDirectory}/../mixins/build/libs/mixins-$version.jar"
    target = "${project.layout.projectDirectory}/../rs/build/libs/rs-$version.jar"
    output = File("${project.layout.projectDirectory}/lib/injected-client.jar")
}

tasks.withType<JavaCompile> {
    dependsOn(tasks.withType<InjectTask>())
}

tasks.withType<KotlinCompile> {
    dependsOn(tasks.withType<InjectTask>())
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.3.0")
    implementation(files(tasks.withType<InjectTask>().first().output))
    implementation(project(":api"))
    implementation(project(":api-rs"))
    implementation(project(":common"))
    implementation(project(":client-common"))
}

