plugins {
    `kotlin-common`
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("com.google.cloud.tools.jib") version "2.7.0"
    kotlin("plugin.spring") version "1.4.21"
}

object DependencyVersions {
    const val PROMETHEUS = "1.6.0"
    const val SPRING_DOC = "1.5.0"
    const val TESTCONTAINERS = "1.15.0"
}

dependencies {
    implementation(project(":dataprovider"))
    implementation(project(":usecase"))
    implementation(project(":domain"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Jackson support for Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring Doc/Swagger UI
    implementation("org.springdoc:springdoc-openapi-kotlin:${DependencyVersions.SPRING_DOC}")
    implementation("org.springdoc:springdoc-openapi-ui:${DependencyVersions.SPRING_DOC}")

    // Prometheus actuator
    implementation("io.micrometer:micrometer-registry-prometheus:${DependencyVersions.PROMETHEUS}")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Testcontainers
    testImplementation(platform("org.testcontainers:testcontainers-bom:${DependencyVersions.TESTCONTAINERS}"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}
