import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
}

// Toolchains:
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// Dependencies:
repositories {
    mavenCentral()
}

val annotationImplementation: Configuration by configurations.creating {
    configurations.compileOnly.get().extendsFrom(this)
    configurations.testCompileOnly.get().extendsFrom(this)
    configurations.annotationProcessor.get().extendsFrom(this)
    configurations.testAnnotationProcessor.get().extendsFrom(this)
}

dependencies {
    // Annotations
    annotationImplementation("org.projectlombok:lombok:1.18.36")

    // Core Libraries
    implementation("it.unimi.dsi:fastutil:8.5.15")
    implementation("net.dv8tion:JDA:6.0.0")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-core:1.5.18")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("org.fusesource.jansi:jansi:2.4.2")
}


// Task:
tasks.compileJava {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.jar {
    archiveClassifier.set("")
    from(sourceSets.main.get().output)
    manifest {
        attributes["Main-Class"] = "me.darragh.axesociety.confinementbot.Main"
        attributes["Implementation-Title"] = project.name
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("all")
    from(sourceSets.main.get().output)
    minimize()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}