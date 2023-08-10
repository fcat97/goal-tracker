plugins {
    id("com.android.application").version("8.1.0-rc01").apply(false)
    id("com.android.library").version("8.1.0-rc01").apply(false)
    kotlin("android").version("1.8.21").apply(false)
    kotlin("multiplatform").version("1.8.21").apply(false)
    id("org.jetbrains.compose").version("1.4.3").apply(false)
    kotlin("plugin.serialization") version "1.8.21" apply false
    id("io.realm.kotlin") version "1.10.0" apply false

}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
