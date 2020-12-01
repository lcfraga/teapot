import configuration.DependencyVersions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    java
}

group = "io.teapot"
version = "0.0.1-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("io.kotest:kotest-assertions-core:${DependencyVersions.KOTEST}")
    testImplementation("io.mockk:mockk:${DependencyVersions.MOCKK}")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(DependencyVersions.JAVA))
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xemit-jvm-type-annotations")
            jvmTarget = DependencyVersions.JAVA
        }
    }

    withType<Jar> {
        manifest {
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}
