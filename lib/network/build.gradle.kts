import com.facetracking.buildsrc.Apps.COMPILE_SDK
import com.facetracking.buildsrc.Apps.MIN_SDK
import com.facetracking.buildsrc.Apps.TARGET_SDK
import com.facetracking.buildsrc.Apps.androidTestInstrumentation
import com.facetracking.buildsrc.Libs.CORE_HILT
import com.facetracking.buildsrc.Libs.HILT_ANDROID_COMPILER
import com.facetracking.buildsrc.Libs.HILT_COMPILER
import com.facetracking.buildsrc.Libs.LOGIN_INTERCEPTOR
import com.facetracking.buildsrc.Libs.MOSHI
import com.facetracking.buildsrc.Libs.RETROFIT
import com.facetracking.buildsrc.Libs.RETROFIT_CONVERTER
import com.facetracking.buildsrc.TestLibs.COROUTINES_TEST
import com.facetracking.buildsrc.TestLibs.JUNIT_LIB
import com.facetracking.buildsrc.TestLibs.MOCK
import com.facetracking.buildsrc.TestLibs.MOCKITO_KOTLIN_LIBRARY
import com.facetracking.buildsrc.TestLibs.MOCK_WEB_SERVER
import com.facetracking.buildsrc.TestLibs.RUNNER

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

    buildTypes {
        forEach {
            it.buildConfigField(
                type = "String",
                name = "API_BASE_URL", value = "\"https://webhook.site/\""
            )
        }
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
    api(LOGIN_INTERCEPTOR)
    api(RETROFIT)
    implementation(project(":lib:model"))
    implementation(RETROFIT_CONVERTER)
    implementation(CORE_HILT)
    implementation(MOSHI)
    implementation(MOCKITO_KOTLIN_LIBRARY)

    kapt(HILT_COMPILER)
    kapt(HILT_ANDROID_COMPILER)

    testImplementation(JUNIT_LIB)
    testImplementation(MOCK)
    testImplementation(RUNNER)
    testImplementation(MOCK_WEB_SERVER)
    testImplementation(COROUTINES_TEST)
}
