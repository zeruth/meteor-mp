plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

version = "2.1.5-SNAPSHOT"

android {
    namespace = "com.meteor.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.meteor.android"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "2.1.2"

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
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "36.0.0 rc4"
    ndkVersion = "28.0.13004108"
}

val acraVersion = "5.11.4"

dependencies {
    implementation(project(":common"))
    implementation(project(":rs2"))
    implementation(project(":android-awt"))

    with(libs) {
        implementation(logger)
        implementation(eventbus)
        implementation(exoplayer)
        implementation(firebase.analytics)
        implementation(firebase.crashlytics)
        implementation(google.firebase.analytics)
        implementation(kotlin.reflect)
        implementation(line.awesome)
        implementation(gson)
        implementation(androidx.core.ktx)
        implementation(androidx.lifecycle.runtime.ktx)
        implementation(androidx.activity.compose)
        implementation(androidx.ui)
        implementation(androidx.ui.graphics)
        implementation(androidx.ui.tooling.preview)
        implementation(androidx.material3)
        implementation(platform(firebase.bom))
        implementation(platform(androidx.compose.bom))

        testImplementation(junit)

        androidTestImplementation(androidx.junit)
        androidTestImplementation(androidx.espresso.core)
        androidTestImplementation(androidx.ui.test.junit4)
        androidTestImplementation(platform(androidx.compose.bom))

        debugImplementation(androidx.ui.tooling)
        debugImplementation(androidx.ui.test.manifest)
    }
}