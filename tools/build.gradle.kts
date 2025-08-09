// Gradle build configuration for the tools module.
// Contains various tool implementations such as weather, time, timers,
// search/news, article reading, and vision.

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.hexo.tools"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":ml"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Use Room for caching weather/search results if needed
    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

    // JSoup is provided from core module, accessible via transitive dependencies
}