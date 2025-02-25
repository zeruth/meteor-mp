plugins {
    id("java")
    kotlin("jvm")
}

version = "2.1.6-SNAPSHOT"

dependencies {
    compileOnly(files("../lib/android-35.jar"))
}

dependencies {
    implementation(project(":api"))
    implementation(project(":logger"))
    implementation(libs.annotations)
    with (libs) {
        implementation(kotlin.coroutines)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}