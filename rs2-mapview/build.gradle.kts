plugins {
    id("java")
    kotlin("jvm")
}

version = "2.1.5"

dependencies {
    compileOnly(files("../lib/android-35.jar"))
    compileOnly(project(":rs2"))
}

dependencies {
    with(libs) {
        compileOnly(java.websocket)
        implementation(project(":eventbus"))
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}