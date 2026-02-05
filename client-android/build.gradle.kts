import com.android.build.gradle.internal.tasks.DexFileDependenciesTask
import org.gradle.kotlin.dsl.withType

plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.application")
    alias(libs.plugins.compose.compiler)
}


group = "io.github.nullpops"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

tasks.configureEach {
    if (name.contains("Dependencies") || name.contains("merge") || name.contains("lint") || name.contains("generate")) {
        dependsOn(":injected-client:injectMultiplatform")
    }
}

dependencies {
    //Project
    implementation(project(":api"))
    implementation(project(":api-rs"))
    implementation(project(":common"))
    implementation(project(":client-common"))

    implementation(files("../lib/injected-client.jar"))

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
