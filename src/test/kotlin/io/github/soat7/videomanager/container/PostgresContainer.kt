package io.github.soat7.videomanager.container

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainer : BeforeAllCallback {

    companion object {
        private const val DATABASE_NAME = "video-manager"
        private const val DB_USERNAME = "it_user"
        private const val DB_PASSWORD = "it_password"
        private val container = PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))
            .apply {
                withDatabaseName(DATABASE_NAME)
                withUsername(DB_USERNAME)
                withPassword(DB_PASSWORD)
                withExposedPorts(5432)
            }

        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun beforeAll(p0: ExtensionContext?) {
        if (!container.isRunning) {
            container.start()
            log.info("Postgres container started with host [${container.jdbcUrl}]")
            System.setProperty("spring.flyway.url", container.jdbcUrl)
            System.setProperty("spring.r2dbc.url", container.jdbcUrl.replace("jdbc", "r2dbc"))
            System.setProperty("spring.r2dbc.username", container.username)
            System.setProperty("spring.r2dbc.password", container.password)
        }
    }
}
