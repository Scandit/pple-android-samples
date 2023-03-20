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
        versionName = rootProject.extra.get("publishVersionName") as String
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

    lint {
        checkDependencies = true
        xmlReport = true
        xmlOutput = file("../lint_reports/${project.name}-lint-report.xml")
        htmlReport = true
        htmlOutput = file("../lint_reports/${project.name}-lint-report.html")
        abortOnError = true
        warningsAsErrors = true
        disable.add("MergeRootFrame")
    }
}

dependencies {
    implementation(files("libs/pple-sdk.aar"))

    implementation(files("libs/ScanditCaptureCore.aar"))
    implementation(files("libs/ScanditBarcodeCapture.aar"))
    implementation(files("libs/ScanditLabelCapture.aar"))
    implementation(files("libs/ScanditTextCaptureBase.aar"))
    implementation(files("libs/ScanditPriceLabel.aar"))

    implementation(deps.androidx.core)
    implementation(deps.appcompat)
    implementation(deps.material)
    implementation(deps.constraintlayout)
    implementation(deps.androidx.lifecycle.livedata)
    implementation(deps.androidx.lifecycle.viewmodel)
    implementation(deps.androidx.lifecycle.runtime)
    implementation(deps.androidx.swiperefreshlayout)
}
