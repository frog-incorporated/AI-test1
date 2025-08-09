// Gradle build configuration for the ml module.
// The ml module contains machine learning interfaces and implementations
// for wake-word detection, speech-to-text and text-to-speech engines.

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.hexo.ml"
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
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // TensorFlow Lite support for on-device inference
    implementation("org.tensorflow:tensorflow-lite:2.13.0")

    // Android SpeechRecognizer dependencies are part of the Android framework
}