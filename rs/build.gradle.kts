import java.util.Properties
import kotlin.apply

plugins {
    kotlin("jvm")
    id("application")
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val androidSdkDir: String? = System.getenv("ANDROID_SDK_ROOT") ?: System.getenv("ANDROID_HOME")

val androidSdkLocalDir = localProperties["sdk.dir"] as String?

dependencies {
    if (androidSdkLocalDir != null) {
        compileOnly(files(
            "$androidSdkLocalDir/platforms/android-36/android.jar"
        ))
    } else
        compileOnly(files(
            "$androidSdkDir/platforms/android-36/android.jar"
        ))
    implementation(libs.kotlin.reflect.v230)
    implementation(project(":common"))
}

application {
    mainClass.set("jagex3.client.Client")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.withType<JavaExec> {
    jvmArgs = listOf("-Dsun.java2d.uiScale=1.0", "-Dsun.java2d.dpiaware=true")
}

