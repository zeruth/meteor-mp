import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "2.3.0"
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

dependencies {
    compileOnly(files("../lib/android-36.jar"))
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    implementation("org.ow2.asm:asm:9.9.1")
    implementation("org.ow2.asm:asm-util:9.9.1")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.guava:guava:33.4.0-jre")
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

