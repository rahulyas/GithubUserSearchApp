plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.0"
    alias(libs.plugins.devToolsKspGoogle)
    alias(libs.plugins.daggerHiltAndroid)
}

android {
    namespace = "com.rahul.githubusersearchapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rahul.githubusersearchapp"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures {
        compose = true
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //
    implementation (libs.androidx.material3.window.size)

    // Retrofit and Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //kotlinx serialization
    implementation(libs.kotlinx.serialization.json)
    implementation( libs.androidx.lifecycle.runtime.compose)
    //dagger hilt
    ksp(libs.com.google.dagger.hilt.compiler)
    implementation(libs.bundles.daggerHilt)
    //paging
    implementation(libs.bundles.pagination)
    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    //SwipeRefresh
    implementation(libs.androidx.material3.pull.refresh)
    // DataStore for theme persistence
    implementation (libs.androidx.datastore.preferences)
}