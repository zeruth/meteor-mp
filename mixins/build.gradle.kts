import java.util.Properties
import kotlin.apply

plugins {
    id("java")
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
    implementation(project(":api"))
    implementation(project(":api-rs"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    disableAutoTargetJvm()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:-options")
}