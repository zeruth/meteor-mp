plugins {
    id("java")
}

version = "2.1.6-SNAPSHOT"

dependencies {
    compileOnly(files("../lib/android-35.jar"))
}

dependencies {
    compileOnly(project(":eventbus"))
    with(libs) {
        compileOnly(java.websocket)
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