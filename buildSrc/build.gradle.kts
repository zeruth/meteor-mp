import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "2.0.0"
}

version = "2.1.5"

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

dependencies {
    compileOnly(files("../lib/android-35.jar"))
    implementation(libs.logger)
    implementation(libs.annotations)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation(libs.asm)
    implementation(libs.asm.util)
    implementation(libs.gson)
    implementation(libs.guava)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

