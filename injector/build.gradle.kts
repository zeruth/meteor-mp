import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    id("java-gradle-plugin")
    with (libs) {
        alias(plugins.org.jetbrains.kotlin.jvm)
    }
}

version = "1.0.0-SNAPSHOT"

gradlePlugin {
    plugins {
        create("injector") {
            id = "nulled.injector"
            implementationClass = "nulled.InjectorPlugin"
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven { url = uri("https://raw.githubusercontent.com/MeteorLite/hosting/main/repo/") }
    maven { url = uri("https://raw.githubusercontent.com/zeruth/repo/main/") }
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
    with (libs) {
        implementation(asm)
        implementation(asm.util)
        implementation(gson)
        implementation(guava)
        annotationProcessor(lombok)
        compileOnly(lombok)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

