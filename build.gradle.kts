
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    // id("io.izzel.taboolib") version "2.0.27"
    kotlin("jvm") version "2.2.0"
}


repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.papermc.io/repository/maven-snapshots/")
    maven("https://repo.tabooproject.org/repository/releases/")
}

dependencies {
    // Spigot 兼容说明：DFU（com.mojang.serialization.*）已全部反射化，
    // DataResult 在 MC 1.21.1 及以前是 class、MC 1.21.2 起是 interface（DFU 8.0.16），
    // 直接引用会因字节码指令差异抛 IncompatibleClassChangeError。
    compileOnly(kotlin("stdlib"))
    compileOnly("com.google.code.gson:gson:2.10.1")
    // 只用到了 org.bukkit.inventory.ItemStack 与 craftbukkit 反射，spigot-api 足够
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {

}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
tasks.withType<JavaCompile> {
    options.release.set(21)
    options.encoding = "UTF-8"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components.findByName("java"))
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()
        }
    }
}