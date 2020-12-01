plugins {
    `kotlin-common`
    `kotlin-library`
    kotlin("plugin.spring") version "1.4.20"
    kotlin("plugin.jpa") version "1.4.20"
    id("org.flywaydb.flyway")
}

dependencies {
    implementation(project(":usecase"))
    implementation(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.4.0")
    implementation("com.vladmihalcea:hibernate-types-52:2.10.0")
    implementation("org.postgresql:postgresql:42.2.18")
    implementation("org.flywaydb:flyway-core:7.2.1")
}
