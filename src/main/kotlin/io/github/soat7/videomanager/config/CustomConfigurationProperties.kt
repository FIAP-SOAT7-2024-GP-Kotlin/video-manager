package io.github.soat7.videomanager.config

import io.github.soat7.videomanager.database.r2dbc.config.DatabaseProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    value = [
        DatabaseProperties::class
    ]
)
class CustomConfigurationProperties
