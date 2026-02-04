plugins {
    kotlin("jvm")
    id("application")
}

group = "io.github.nullpops"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.3.0")
    implementation(project(":common"))
    compileOnly(files("../lib/android-36.jar"))
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