plugins {
    id("java")
}

version = "2.1.5-SNAPSHOT"

dependencies {
    compileOnly(files("../lib/android-35.jar"))
}

dependencies {

    with(libs) {
        compileOnly(java.websocket)
        compileOnly(eventbus)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_6
    targetCompatibility = JavaVersion.VERSION_1_6

    disableAutoTargetJvm()
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
}