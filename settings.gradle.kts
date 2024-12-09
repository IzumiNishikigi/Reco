@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()           // Required for Android-specific plugins
        mavenCentral()     // Maven Central for other plugins
        gradlePluginPortal() // Gradle Plugin Portal for general plugins
    }
}

dependencyResolutionManagement {
    repositories {
        google()           // Google's Maven repository for Android libraries
        mavenCentral()     // Maven Central for most open-source libraries
    }
}

rootProject.name = "Reco"
include(":app")
