plugins {
    id("java")
}

group = "io.github.nullpops"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("../lib/android-36.jar"))
}

dependencies {
    implementation(project(":common"))
    implementation(project(":api"))
    implementation(project(":api-rs"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    disableAutoTargetJvm()
}