plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}


dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}
gradlePlugin{
    plugins{
        register("petCommunityApplicationConventionPlugin"){
            id = "pet-community-android-application"
            implementationClass = "PetCommunityApplicationConventionPlugin"
        }

        register("petCommunityLibraryConventionPlugin"){
            id = "pet-community-android-library"
            implementationClass = "PetCommunityLibraryConventionPlugin"
        }
        register("hiltConventionPlugin"){
            id = "hilt-convention-plugin"
            implementationClass = "HiltConventionPlugin"
        }
    }
}
