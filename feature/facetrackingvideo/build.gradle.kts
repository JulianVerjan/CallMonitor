import com.facetracking.buildsrc.Apps.COMPILE_SDK
import com.facetracking.buildsrc.Apps.MIN_SDK
import com.facetracking.buildsrc.Apps.TARGET_SDK
import com.facetracking.buildsrc.Apps.androidTestInstrumentation
import com.facetracking.buildsrc.Libs.APP_COMPAT
import com.facetracking.buildsrc.Libs.FACE_DETECTION
import com.facetracking.buildsrc.Libs.CAMERA_2
import com.facetracking.buildsrc.Libs.CAMERA_CORE
import com.facetracking.buildsrc.Libs.CAMERA_LIFE_CYCLE
import com.facetracking.buildsrc.Libs.CAMERA_VIEW
import com.facetracking.buildsrc.Libs.CONSTRAINT_LAYOUT
import com.facetracking.buildsrc.Libs.CORE
import com.facetracking.buildsrc.Libs.CORE_HILT
import com.facetracking.buildsrc.Libs.COROUTINES_ANDROID
import com.facetracking.buildsrc.Libs.COROUTINES_CORE
import com.facetracking.buildsrc.Libs.FRAGMENT_KTX
import com.facetracking.buildsrc.Libs.FRAGMENT_NAVIGATION
import com.facetracking.buildsrc.Libs.HILT_ANDROID_COMPILER
import com.facetracking.buildsrc.Libs.HILT_COMPILER
import com.facetracking.buildsrc.Libs.KOTLIN_JDK
import com.facetracking.buildsrc.Libs.MATERIAL
import com.facetracking.buildsrc.Libs.NAVIGATION_LAYOUT
import com.facetracking.buildsrc.Libs.NAVIGATION_UI_LIB
import com.facetracking.buildsrc.TestLibs.ARCH_CORE
import com.facetracking.buildsrc.TestLibs.COROUTINES_TEST
import com.facetracking.buildsrc.TestLibs.JUNIT_LIB
import com.facetracking.buildsrc.TestLibs.MOCK
import com.facetracking.buildsrc.TestLibs.MOCKITO_INLINE
import com.facetracking.buildsrc.TestLibs.MOCKITO_KOTLIN_LIBRARY

plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android-extensions")
    id("androidx.navigation.safeargs.kotlin")
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

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    lintOptions {
        lintConfig = rootProject.file(".lint/config.xml")
        isCheckAllWarnings = true
        isWarningsAsErrors = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(KOTLIN_JDK)
    api(project(":lib:data"))
    api(project(":lib:network"))
    implementation(project(":commons:resources"))
    implementation(project(":lib:model"))
    implementation(APP_COMPAT)
    implementation(MATERIAL)
    implementation(CORE)
    implementation(NAVIGATION_UI_LIB)
    implementation(COROUTINES_CORE)
    implementation(COROUTINES_ANDROID)
    implementation(CONSTRAINT_LAYOUT)
    implementation(FRAGMENT_KTX)
    implementation(FRAGMENT_NAVIGATION)
    implementation(NAVIGATION_LAYOUT)
    implementation(MATERIAL)
    implementation(MOCKITO_INLINE)
    implementation(MOCKITO_KOTLIN_LIBRARY)
    implementation(CORE_HILT)

    implementation(CAMERA_2)
    implementation(CAMERA_CORE)
    implementation(CAMERA_LIFE_CYCLE)
    implementation(CAMERA_VIEW)
    implementation(FACE_DETECTION)

    kapt(HILT_COMPILER)
    kapt(HILT_ANDROID_COMPILER)
    testImplementation(JUNIT_LIB)
    testImplementation(MOCK)
    testImplementation(ARCH_CORE)
    testImplementation("org.mockito:mockito-inline:3.8.0")
    testImplementation(COROUTINES_TEST)
}
