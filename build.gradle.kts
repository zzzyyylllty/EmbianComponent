
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    // id("io.izzel.taboolib") version "2.0.27"
    kotlin("jvm") version "1.8.22"
}


repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://repo.tabooproject.org/repository/releases/")
}

dependencies {
    compileOnly("ink.ptms.core:v12104:12104:mapped")
    compileOnly("ink.ptms.core:v12104:12104:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly("com.google.code.gson:gson:2.10.1")
//    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
