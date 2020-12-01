plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

repositories {
    gradlePluginPortal()
}

object Plugins {
    const val KOTLIN = "1.4.20"
    const val DETEKT = "1.15.0-RC1"
    const val KORDAMP = "0.41.0"
    const val FLYWAY = "7.2.1"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Plugins.KOTLIN}")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Plugins.DETEKT}")
    implementation("org.kordamp.gradle:jacoco-gradle-plugin:${Plugins.KORDAMP}")
    implementation("org.kordamp.gradle:testing-gradle-plugin:${Plugins.KORDAMP}")
    implementation("gradle.plugin.org.flywaydb:gradle-plugin-publishing:${Plugins.FLYWAY}")
}
