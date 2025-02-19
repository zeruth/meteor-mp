plugins {
    id("java")
    kotlin("jvm")
}

version = "2.1.5-SNAPSHOT"

dependencies {
    compileOnly(files("../lib/android-35.jar"))
}

dependencies {
    implementation(project(":api"))
    implementation(libs.annotations)
    implementation(libs.logger)
    with (libs) {
        implementation(kotlin.coroutines)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}