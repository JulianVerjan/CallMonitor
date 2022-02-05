import com.callmonitor.buildsrc.Apps.COMPILE_SDK
import com.callmonitor.buildsrc.Apps.MIN_SDK
import com.callmonitor.buildsrc.Apps.TARGET_SDK
import com.callmonitor.buildsrc.Libs.CORE_HILT
import com.callmonitor.buildsrc.Libs.HILT_ANDROID_COMPILER
import com.callmonitor.buildsrc.Libs.KOTLIN_JDK
import com.callmonitor.buildsrc.Libs.KTOR_GSON
import com.callmonitor.buildsrc.Libs.KTOR_SERVER
import com.callmonitor.buildsrc.Libs.KTOR_SERVER_NETTY
import com.callmonitor.buildsrc.Libs.KTOR_WEB_SOCKETS
import com.callmonitor.buildsrc.Libs.ROOM_COMPILER
import com.callmonitor.buildsrc.Libs.ROOM_EXTENSIONS
import com.callmonitor.buildsrc.Libs.ROOM_RUNTIME
import com.callmonitor.buildsrc.TestLibs.COROUTINES_TEST
import com.callmonitor.buildsrc.TestLibs.JUNIT_LIB

plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {

    compileSdkVersion(COMPILE_SDK)

    defaultConfig {
        minSdkVersion(MIN_SDK)
        targetSdkVersion(TARGET_SDK)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    implementation(ROOM_RUNTIME)
    annotationProcessor(ROOM_COMPILER)
    kapt(ROOM_COMPILER)
    implementation(ROOM_EXTENSIONS)

    implementation(KOTLIN_JDK)
    implementation(project(":lib:model"))
    implementation(CORE_HILT)

    implementation(KTOR_SERVER)
    implementation(KTOR_SERVER_NETTY)
    implementation(KTOR_WEB_SOCKETS)
    implementation(KTOR_GSON)

    kapt(HILT_ANDROID_COMPILER)

    testImplementation(JUNIT_LIB)
    testImplementation(COROUTINES_TEST)
}