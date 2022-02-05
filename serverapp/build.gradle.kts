import com.callmonitor.buildsrc.Apps.COMPILE_SDK
import com.callmonitor.buildsrc.Apps.MIN_SDK
import com.callmonitor.buildsrc.Apps.SERVER_APP_ID
import com.callmonitor.buildsrc.Apps.TARGET_SDK
import com.callmonitor.buildsrc.Libs.CONSTRAINT_LAYOUT
import com.callmonitor.buildsrc.Libs.CORE
import com.callmonitor.buildsrc.Libs.CORE_HILT
import com.callmonitor.buildsrc.Libs.FRAGMENT_KTX
import com.callmonitor.buildsrc.Libs.HILT_ANDROID_COMPILER
import com.callmonitor.buildsrc.Libs.HILT_LIFE_CYCLE
import com.callmonitor.buildsrc.Libs.KOTLIN_JDK
import com.callmonitor.buildsrc.Libs.NAVIGATION_UI_LIB
import com.callmonitor.buildsrc.TestLibs.ESPRESSO_LIB
import com.callmonitor.buildsrc.TestLibs.JUNIT_EXT
import com.callmonitor.buildsrc.TestLibs.JUNIT_LIB

plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {

    compileSdkVersion(COMPILE_SDK)
    defaultConfig {
        applicationId(SERVER_APP_ID)
        minSdkVersion(MIN_SDK)
        targetSdkVersion(TARGET_SDK)
        buildToolsVersion(buildToolsVersion)
        versionCode = versionCode
        versionName = versionName

        testInstrumentationRunner = com.callmonitor.buildsrc.Apps.androidTestInstrumentation
    }

    buildTypes {
        getByName(com.callmonitor.buildsrc.BuildType.RELEASE) {
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
            isMinifyEnabled = com.callmonitor.buildsrc.BuildTypeRelease.isMinifyEnabled
            isTestCoverageEnabled = com.callmonitor.buildsrc.BuildTypeRelease.isTestCoverageEnabled
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    packagingOptions {
        exclude("META-INF/*")
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
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
    implementation(project(":lib:server"))
    implementation(KOTLIN_JDK)
    implementation(CORE)
    implementation(CORE_HILT)
    implementation(NAVIGATION_UI_LIB)
    implementation(FRAGMENT_KTX)
    implementation(CONSTRAINT_LAYOUT)

    kapt(HILT_LIFE_CYCLE)
    kapt(HILT_ANDROID_COMPILER)

    testImplementation(JUNIT_LIB)
    androidTestImplementation(ESPRESSO_LIB)
    androidTestImplementation(JUNIT_EXT)
}