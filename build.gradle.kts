buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
        classpath("com.android.tools.build:gradle:8.5.1")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
