import nulled.InjectTask
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.application")
    alias(libs.plugins.compose.compiler)
    id("nulled.injector")
}


group = "io.github.nullpops"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

tasks.withType<InjectTask> {
    outputs.upToDateWhen {
        false
    }
    dependsOn(":api-rs:build")
    dependsOn(":mixins:build")
    dependsOn(":rs:build")
    api = "${project.layout.projectDirectory}/../api-rs/build/classes/java/main/net/runelite/rs/api/"
    mixins = "${project.layout.projectDirectory}/../mixins/build/libs/mixins-$version.jar"
    target = "${project.layout.projectDirectory}/../rs/build/libs/rs-$version.jar"
    output = File("${project.layout.projectDirectory}/lib/injected-client.jar")
}

val injectTask = tasks.withType<InjectTask>().named("inject")

dependencies {
    //Project
    implementation(project(":api"))
    implementation(project(":api-rs"))
    implementation(project(":common"))
    implementation(project(":client-common"))

    //Injected rs
    implementation(
        files(injectTask.map { it.output })
            .builtBy(injectTask)
    )

    //AndroidX Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)

    implementation("org.jetbrains.kotlin:kotlin-reflect:2.3.0")
}

android {
    namespace = "com.meteor.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.meteor.android"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "3.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
