plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.androidlab2_tasks"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.androidlab2_tasks"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.espresso.core)


    // Java language implementation
    implementation(libs.androidx.core)
    // Kotlin
    implementation(libs.core.ktx)

    // To use RoleManagerCompat
    implementation(libs.androidx.core.role)

    // To use the Animator APIs
    implementation(libs.androidx.core.animation)
    implementation(libs.androidx.work.runtime.ktx)
    // To test the Animator APIs
    androidTestImplementation(libs.androidx.core.animation.testing)

    // Optional - To enable APIs that query the performance characteristics of GMS devices.
    implementation(libs.androidx.core.performance)

    // Optional - to use ShortcutManagerCompat to donate shortcuts to be used by Google
    implementation(libs.androidx.core.google.shortcuts)

    // Optional - to support backwards compatibility of RemoteViews
    implementation(libs.androidx.core.remoteviews)

    // Optional - APIs for SplashScreen, including compatibility helpers on devices prior Android 12
    implementation(libs.androidx.core.splashscreen)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}