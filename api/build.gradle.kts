plugins {
    id("java")
}

version = "2.1.5-SNAPSHOT"

dependencies {
    compileOnly(files("../lib/android-35.jar"))
}

dependencies {
    implementation(libs.logger)
    implementation(libs.annotations)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}