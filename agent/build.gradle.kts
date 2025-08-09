// Gradle build configuration for the agent module.
// Contains orchestrator logic including the router, tool runner, prompt
// builder, and speaker for streaming responses.

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.hexo.agent"
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
    implementation(project(":tools"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
}