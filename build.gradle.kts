// Top level build file for the Hexo project.
// This file configures buildscript-wide plugins and repositories and applies
// shared configuration to all subprojects.

plugins {
    // We declare the Android and Kotlin plugins here with `apply false` so
    // that they can be applied in individual modules without re-declaring
    // their versions in each module. When changing plugin versions, only
    // update them here.
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    kotlin("android") version "1.9.0" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}