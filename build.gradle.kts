// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlin) apply false
    alias(libs.plugins.hiltAndroid) version "2.50" apply false
    alias(libs.plugins.googleServices) version "4.4.0" apply false
    alias(libs.plugins.androidLibrary) apply false

}