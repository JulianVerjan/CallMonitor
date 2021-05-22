import com.facetracking.buildsrc.Apps.COMPILE_SDK
import com.facetracking.buildsrc.Apps.MIN_SDK
import com.facetracking.buildsrc.Apps.TARGET_SDK
import com.facetracking.buildsrc.Apps.androidTestInstrumentation

plugins {
    id("com.android.library")
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
}
