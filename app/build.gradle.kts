import nulled.InjectTask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("nulled.injector") version "1.5"
}

android {
    namespace = "com.meteor.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.meteor.android"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.withType<InjectTask> {
    outputs.upToDateWhen {
        false
    }
    dependsOn(":api-rs:build")
    dependsOn(":mixins:build")
    dependsOn(":deob:build")
    api = "${project.layout.projectDirectory}/../api-rs/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes/net/runelite/rs/api/"
    mixins = "${project.layout.projectDirectory}/../mixins/build/intermediates/full_jar/debug/createFullJarDebug/full.jar"
    target = "${project.layout.projectDirectory}/../deob/build/intermediates/full_jar/debug/createFullJarDebug/full.jar"
    output = "${project.layout.projectDirectory}/lib/injected-client.jar"
}

val acraVersion = "5.11.4"

dependencies {

    implementation("nulled:logger:1.2")
    implementation("nulled:eventbus:1.1")
    implementation(files("./lib/injected-client.jar"))
    implementation(project(":api"))
    implementation(project(":api-rs"))
    implementation(project(":native-awt"))
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.0")
    implementation("br.com.devsrsouza.compose.icons:line-awesome:1.1.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}