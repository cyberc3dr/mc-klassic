plugins {
    application
    alias(libs.plugins.kotlin)
}

group = "ru.sliva"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("ru.sliva.mcklassic.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.coroutines)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.korge.core)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release = 21
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<Jar> {
    destinationDirectory = file("$rootDir/build")
}

sourceSets.main {
    kotlin.srcDir("src")
    resources.srcDir("resources")
}