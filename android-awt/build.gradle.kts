plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

version = "2.1.5-SNAPSHOT"

android {
    namespace = "com.meteor.nat.awt"
    compileSdk = 35

    defaultConfig {
        minSdk = 30
        lint.targetSdk = 35
    }

    //Note we must always use 1_8 here as it's the only version where we can spoof the java.awt classes
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
    buildToolsVersion = "36.0.0 rc5"
    ndkVersion = "28.0.13004108"
}

dependencies {
    with(libs) {
        implementation(sfntly)
        implementation(androidx.core.ktx)
        implementation(androidx.appcompat)
        implementation(material)
        testImplementation(junit)
        androidTestImplementation(androidx.junit)
        androidTestImplementation(androidx.espresso.core)
    }
}