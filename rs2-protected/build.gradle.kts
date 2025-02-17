plugins {
    id("java")
}

version = "2.1.5-SNAPSHOT"

dependencies {
    compileOnly(project(":rs2"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_6
    targetCompatibility = JavaVersion.VERSION_1_6

    disableAutoTargetJvm()
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
}