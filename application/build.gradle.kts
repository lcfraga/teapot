plugins {
    `kotlin-common`
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("com.google.cloud.tools.jib") version "2.6.0"
    kotlin("plugin.spring") version "1.4.20"
}

object DependencyVersions {
    const val PROMETHEUS = "1.6.0"
}

dependencies {
    implementation(project(":dataprovider"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Jackson support for Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Prometheus actuator
    implementation("io.micrometer:micrometer-registry-prometheus:${DependencyVersions.PROMETHEUS}")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
