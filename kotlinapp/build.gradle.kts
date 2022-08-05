plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 32

    namespace = "com.scandit.shelf.kotlinapp"
    defaultConfig {
        applicationId = "com.scandit.shelf.kotlinapp"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(files("libs/pple-sdk.aar"))
    implementation(files("libs/pple-data.aar"))

    implementation(files("libs/pple-contracts.jar"))
    implementation(files("libs/pple-domain.jar"))
    implementation(files("libs/pple-entities.jar"))

    implementation(files("libs/ScanditCaptureCore.aar"))
    implementation(files("libs/ScanditBarcodeCapture.aar"))
    implementation(files("libs/ScanditLabelCapture.aar"))
    implementation(files("libs/ScanditTextCaptureBase.aar"))
    implementation(files("libs/ScanditPriceLabel.aar"))

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.startup:startup-runtime:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
}
