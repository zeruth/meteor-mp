plugins {
    id("java")
}

version = "2.1.2"

dependencies {
    compileOnly(files("../lib/android-35.jar"))
}

dependencies {

    with(libs) {
        implementation(java.websocket)
        compileOnly(eventbus)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}