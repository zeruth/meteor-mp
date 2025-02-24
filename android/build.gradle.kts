import com.android.build.gradle.internal.tasks.BaseTask
import nulled.InjectTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("nulled.injector")
}

version = "2.1.5-SNAPSHOT"

tasks.withType<InjectTask> {
    outputs.upToDateWhen {
        false
    }
    dependsOn(":api-rs:build")
    dependsOn(":mixins:build")
    dependsOn(":rs2:build")
    api = "${project.layout.projectDirectory}/../api-rs/build/classes/java/main/net/runelite/rs/api/"
    mixins = "${project.layout.projectDirectory}/../mixins/build/libs/mixins-$version.jar"
    target = "${project.layout.projectDirectory}/../rs2/build/libs/rs2-$version.jar"
    output = "${project.layout.projectDirectory}/lib/injected-client.jar"
}

tasks.withType<BaseTask> {
    dependsOn("inject")
}

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
    implementation(project(":api"))
    implementation(project(":api-rs"))
    implementation(project(":logger"))
    implementation(project(":android-awt"))
    implementation(project(":eventbus"))
    implementation(project(":rs2-mapview"))
    implementation(files("./lib/injected-client.jar"))

    with(libs) {
        implementation(exoplayer)
        implementation(firebase.analytics)
        implementation(firebase.crashlytics)
        implementation(google.firebase.analytics)
        implementation(kotlin.reflect)
        implementation(line.awesome)
        implementation(gson)
        implementation(java.websocket)
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