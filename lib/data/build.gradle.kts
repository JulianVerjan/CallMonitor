import com.callmonitor.buildsrc.Apps.COMPILE_SDK
import com.callmonitor.buildsrc.Apps.MIN_SDK
import com.callmonitor.buildsrc.Apps.TARGET_SDK
import com.callmonitor.buildsrc.Apps.androidTestInstrumentation
import com.callmonitor.buildsrc.Libs.CORE_HILT
import com.callmonitor.buildsrc.Libs.COROUTINES_CORE
import com.callmonitor.buildsrc.Libs.HILT_ANDROID_COMPILER
import com.callmonitor.buildsrc.TestLibs.COROUTINES_TEST
import com.callmonitor.buildsrc.TestLibs.JUNIT_LIB
import com.callmonitor.buildsrc.TestLibs.MOCK
import com.callmonitor.buildsrc.TestLibs.MOCKITO_KOTLIN_LIBRARY
import com.callmonitor.buildsrc.TestLibs.MOCK_WEB_SERVER
import com.callmonitor.buildsrc.TestLibs.RULES
import com.callmonitor.buildsrc.TestLibs.RUNNER

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(COMPILE_SDK)

    defaultConfig {
        minSdkVersion(MIN_SDK)
        targetSdkVersion(TARGET_SDK)
        testInstrumentationRunner = androidTestInstrumentation
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
        getByName("test") {
            java.srcDir("src/test/kotlin")
        }
        getByName("androidTest") {
            java.srcDir("src/androidTest/kotlin")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(project(":lib:network"))
    implementation(project(":lib:model"))
    implementation(project(":lib:server"))
    implementation(CORE_HILT)
    implementation(COROUTINES_CORE)
    implementation(MOCKITO_KOTLIN_LIBRARY)

    kapt(HILT_ANDROID_COMPILER)

    testImplementation(JUNIT_LIB)
    testImplementation(MOCK)
    testImplementation(RULES)
    testImplementation(RUNNER)
    testImplementation(MOCK_WEB_SERVER)
    testImplementation(COROUTINES_TEST)
}
