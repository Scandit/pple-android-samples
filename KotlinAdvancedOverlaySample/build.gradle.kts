plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 33

    namespace = "com.scandit.shelf.kotlinadvancedoverlaysample"
    defaultConfig {
        applicationId = "com.scandit.shelf.kotlinadvancedoverlaysample"
        minSdk = 24
        targetSdk = 33
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

    implementation(files("libs/ScanditCaptureCore.aar"))
    implementation(files("libs/ScanditBarcodeCapture.aar"))
    implementation(files("libs/ScanditLabelCapture.aar"))
    implementation(files("libs/ScanditPriceLabel.aar"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.startup:startup-runtime:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    implementation("androidx.security:security-crypto:1.1.0-alpha04")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
}
