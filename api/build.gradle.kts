plugins {
    id("java")
    kotlin("jvm")
}

version = "2.1.5"

dependencies {
    compileOnly(files("../lib/android-35.jar"))
}

dependencies {
    compileOnly(project(":logger"))
    implementation(libs.annotations)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}