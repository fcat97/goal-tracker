import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()
    sourceSets {
        val jvmMain by getting  {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":shared"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "media.uqab.goaltracker.MainKt"

        // https://github.com/JetBrains/compose-multiplatform/issues/2668#issuecomment-1419178642
        buildTypes.release {
            proguard {
                configurationFiles.from("compose-desktop.pro")
            }
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Goal Tracker"
            packageVersion = "1.0.0"
        }
    }
}