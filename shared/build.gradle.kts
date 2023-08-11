plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("io.realm.kotlin")
    id("org.jetbrains.compose") version "1.4.3"
    kotlin("plugin.serialization")

}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm("desktop")

    /*listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }*/

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("io.realm.kotlin:library-base:1.10.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                implementation("com.russhwolf:multiplatform-settings-no-arg:1.0.0")

                // implementation("io.realm.kotlin:library-sync:1.10.0") // If using Device Sync
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                //implementation("javax.sound:javax.sound.sampled:1.3.0")
            }
        }
    }
}

android {
    namespace = "media.uqab.goaltracker"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }
    sourceSets {
        named("main") {
            res.srcDirs("src/commonMain/resources")
        }
    }
}