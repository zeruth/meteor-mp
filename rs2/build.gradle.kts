plugins {
    id("java")
}

dependencies {
    compileOnly(files("../lib/android-35.jar"))
}

dependencies {
    with(libs) {
        compileOnly(eventbus)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}