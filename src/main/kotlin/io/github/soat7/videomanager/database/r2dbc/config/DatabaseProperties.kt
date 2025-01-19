package io.github.soat7.videomanager.database.r2dbc.config

import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.r2dbc")
class DatabaseProperties(
    val driver: String,
    val name: String,
    val url: String,
    val username: String,
    val password: String,
    val pool: R2dbcProperties.Pool
)
