package com.example.petcommunity

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.internal.impldep.com.jcraft.jsch.ConfigRepository.defaultConfig
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*,*,*,*,*>) {
    commonExtension.apply {
        compileSdk = PetCommunityAppConfig.compileSdk
        defaultConfig {
            minSdk = PetCommunityAppConfig.minSDK

        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach{
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }


    }


}