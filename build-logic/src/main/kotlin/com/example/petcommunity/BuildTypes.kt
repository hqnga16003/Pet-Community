package com.example.petcommunity

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureBuildType(commonExtension: CommonExtension<*, *, *, *, *>) {
    commonExtension.apply {
        signingConfigs {

        }
        defaultConfig {


        }
        buildTypes {

            getByName("debug") {
                isMinifyEnabled = false
            }

            getByName("release") {
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )



            }
        }
    }
}