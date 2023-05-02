plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.fuzzing"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("utbot.jar"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}