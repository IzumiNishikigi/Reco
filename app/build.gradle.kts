plugins {
    id("com.android.application") version "8.7.3" apply true
    kotlin("android") version "2.0.21" apply true
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply true // KSP plugin for Kotlin Symbol Processing
}


repositories {

    google()   // Required for KSP and other Android dependencies
    mavenCentral() // Ensure Maven Central is also included for KSP and other libraries

    maven {
        url = uri("https://jitpack.io")
    }



}

android {
    namespace = "com.reco.applock"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.reco.applock"
        minSdk = 33
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

    kotlin {
        jvmToolchain(11) // Ensure JVM compatibility
    }

    buildFeatures {
        viewBinding = true // Enable view binding
    }


}

dependencies {

    // Replace legacy support libraries with AndroidX equivalents
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.material:material:1.12.0")

    // Other dependencies
    implementation("com.github.codebymi:PatternLockView:1.0.2")
    implementation("com.github.bilkeonur:PinLockView:1.0.0")
    // Ensure these libraries are compatible with AndroidX or use Jetifier

    // CameraX dependencies
    implementation(libs.camera.core)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    implementation(libs.navigation.runtime)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment)

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Picasso for image loading
    implementation(libs.picasso) // Picasso's latest version

}
