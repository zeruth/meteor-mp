import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("jvm")
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
    implementation(project(":common"))

    with (libs) {
        compileOnly(compose.runtime)
        compileOnly(compose.ui)
        implementation(composeIcons.lineAwesome)
        implementation(material3)
    }
    compileOnly(compose.desktop.currentOs)
}