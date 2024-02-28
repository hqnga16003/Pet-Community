
plugins {
    id("pet-community-android-application")
    id("hilt-convention-plugin")
    alias(libs.plugins.googleServices)
}

android {
    namespace = "com.example.petcommunity"
    signingConfigs{
        create("config"){
            keyAlias = "key"
            keyPassword = "123456"
            storeFile = rootProject.file("key.jks")
            storePassword = "123456"
        }
    }
    defaultConfig {
        applicationId = "com.example.petcommunity"
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        compileSdkPreview = "UpsideDownCake"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.tooling.manifest)

    //navigation
    implementation(libs.androidx.navigation.compose)

    //firebase
    implementation(platform(libs.com.google.firebase.bom))
    implementation(libs.com.google.firebase.auth)
    implementation(libs.com.google.firebase.storage)
    implementation(libs.com.google.firebase.firestore)

    //hilt
    implementation (libs.hilt.android)
    annotationProcessor (libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // datastore
    implementation(libs.androidx.datastore.preferences)

    //splash
    implementation(libs.androidx.core.splashscreen)
    //coil
    implementation(libs.coil.compose)
    //date time picker
    implementation (libs.datetime)
}
