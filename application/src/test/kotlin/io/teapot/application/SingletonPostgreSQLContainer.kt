package io.teapot.application

import org.testcontainers.containers.PostgreSQLContainer

object SingletonPostgreSQLContainer : PostgreSQLContainer<SingletonPostgreSQLContainer>("postgres:13.1-alpine") {

    override fun start() {
        super.start()

        mapOf(
            "spring.datasource.url" to jdbcUrl,
            "spring.datasource.username" to username,
            "spring.datasource.password" to password
        ).forEach(System::setProperty)
    }

    override fun stop() {
        // Do nothing. Let the JVM clean up.
    }
}
