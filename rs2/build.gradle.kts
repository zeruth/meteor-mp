plugins {
    id("java")
}

dependencies {
    compileOnly(files("./libs/eventbus-1.1.jar"))
    compileOnly(files("../lib/android-35.jar"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}