import com.facetracking.buildsrc.Apps.APP_ID
import com.facetracking.buildsrc.Apps.COMPILE_SDK
import com.facetracking.buildsrc.Apps.MIN_SDK
import com.facetracking.buildsrc.Apps.TARGET_SDK
import com.facetracking.buildsrc.Libs.CONSTRAINT_LAYOUT
import com.facetracking.buildsrc.Libs.CORE
import com.facetracking.buildsrc.Libs.CORE_HILT
import com.facetracking.buildsrc.Libs.FRAGMENT_KTX
import com.facetracking.buildsrc.Libs.FRAGMENT_NAVIGATION
import com.facetracking.buildsrc.Libs.HILT_ANDROID_COMPILER
import com.facetracking.buildsrc.Libs.HILT_COMPILER
import com.facetracking.buildsrc.Libs.KOTLIN_JDK
import com.facetracking.buildsrc.Libs.NAVIGATION_LAYOUT
import com.facetracking.buildsrc.Libs.NAVIGATION_UI_LIB
import com.facetracking.buildsrc.TestLibs.ESPRESSO_LIB
import com.facetracking.buildsrc.TestLibs.JUNIT_EXT
import com.facetracking.buildsrc.TestLibs.JUNIT_LIB

plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {

    compileSdkVersion(COMPILE_SDK)
    defaultConfig {
        applicationId(APP_ID)
        minSdkVersion(MIN_SDK)
        targetSdkVersion(TARGET_SDK)
        buildToolsVersion(buildToolsVersion)
        versionCode = versionCode
        versionName = versionName

        testInstrumentationRunner = com.facetracking.buildsrc.Apps.androidTestInstrumentation
    }

    buildTypes {
        getByName(com.facetracking.buildsrc.BuildType.RELEASE) {
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
            isMinifyEnabled = com.facetracking.buildsrc.BuildTypeRelease.isMinifyEnabled
            isTestCoverageEnabled = com.facetracking.buildsrc.BuildTypeRelease.isTestCoverageEnabled
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":commons:resources"))
    implementation(project(":feature:facetrackingvideo"))
    implementation(KOTLIN_JDK)
    implementation(CORE)
    implementation(CORE_HILT)
    implementation(NAVIGATION_UI_LIB)
    implementation(FRAGMENT_KTX)
    implementation(FRAGMENT_NAVIGATION)
    implementation(NAVIGATION_LAYOUT)
    implementation(CONSTRAINT_LAYOUT)

    kapt(HILT_COMPILER)
    kapt(HILT_ANDROID_COMPILER)

    testImplementation(JUNIT_LIB)
    androidTestImplementation(ESPRESSO_LIB)
    androidTestImplementation(JUNIT_EXT)
}