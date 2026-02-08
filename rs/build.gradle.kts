import java.util.Properties
import kotlin.apply

plugins {
    kotlin("jvm")
    id("application")
}

group = "io.github.nullpops"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val androidSdkDir = localProperties["sdk.dir"] as String

dependencies {
    compileOnly(files(
        "$androidSdkDir/platforms/android-36/android.jar"
    ))
    implementation(libs.kotlin.reflect.v230)
    implementation(project(":common"))
}

application {
    mainClass.set("jagex2.client.Client")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.withType<JavaExec> {
    jvmArgs = listOf("-Dsun.java2d.uiScale=1.0", "-Dsun.java2d.dpiaware=true")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    disableAutoTargetJvm()
}