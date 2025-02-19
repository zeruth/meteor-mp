plugins {
    id("java")
    kotlin("jvm")
}

version = "2.1.5-SNAPSHOT"

dependencies {
    compileOnly(files("../lib/android-35.jar"))
}

dependencies {
    with(libs) {
        compileOnly(java.websocket)
        compileOnly(project(":eventbus"))
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}