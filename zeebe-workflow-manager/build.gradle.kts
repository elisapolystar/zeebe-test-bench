plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.4.21"
    id("org.springframework.boot") version "3.1.3"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.camunda:zeebe-client-java:8.2.11")
    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.7.8"))
    implementation("io.camunda:spring-zeebe-starter:8.2.4")
    implementation("org.slf4j:slf4j-log4j12:2.0.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.camunda.spring:spring-boot-starter-camunda-test:8.2.4")
    testImplementation("ch.qos.logback:logback-classic:1.4.11")
}

tasks.test {
    useJUnitPlatform()
}

