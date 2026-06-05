plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sonarqube)
//    alias(libs.plugins.kotlin.android)
    jacoco
}

android {
    namespace = "com.nityansh.data"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            debug {
                enableUnitTestCoverage = true
                enableAndroidTestCoverage = true
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    api(project(":core:domain"))

    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    // Standard MockK dependency for local unit tests
    testImplementation(libs.mockk) // Use the latest stable version

    // You'll also want the Kotlin coroutines test library for suspend functions
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.turbine)
    androidTestImplementation(libs.turbine)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation (libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.espresso.core)
}

//apply(from = "${rootProject.projectDir}/jacoco.gradle.kts")
jacoco {
    toolVersion = "0.8.12"
}