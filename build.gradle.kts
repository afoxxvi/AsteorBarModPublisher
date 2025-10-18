plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.2.0"
    id("io.ktor.plugin") version "3.3.0"
}

group = "com.afoxxvi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-client-logging")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}