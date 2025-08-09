// Gradle build configuration for the core library module.
// The core module hosts shared utilities such as networking, data storage,
// audio utilities, and general-purpose helpers.

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.hexo.core"
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
    // Kotlin extensions and coroutines support
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Networking and serialization
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    // DataStore for settings persistence
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // WorkManager for timers and background tasks
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // For HTML parsing (search, RSS and articles)
    implementation("org.jsoup:jsoup:1.16.1")
}