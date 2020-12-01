import configuration.DependencyVersions

plugins {
    `java-library`
}

tasks.withType<Jar> {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:${DependencyVersions.KOTEST}")
    testImplementation("io.kotest:kotest-property:${DependencyVersions.KOTEST}")
}
