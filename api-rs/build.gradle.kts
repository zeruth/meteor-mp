plugins {
    id("java")
    kotlin("jvm")
}

dependencies {
    compileOnly(project(":api"))
    implementation(project(":common"))
}