plugins {
    id("java")
}

version = "2.1.5-SNAPSHOT"

dependencies {
    compileOnly(files("../lib/android-35.jar"))
}

dependencies {
    implementation(project(":api"))
    implementation(project(":api-rs"))
    implementation("nulled:logger:1.2")
    implementation("nulled:annotations:1.0")
    implementation("nulled:eventbus:1.1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}