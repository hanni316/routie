// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("jvm") version "1.8.10" apply false
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    }
}






