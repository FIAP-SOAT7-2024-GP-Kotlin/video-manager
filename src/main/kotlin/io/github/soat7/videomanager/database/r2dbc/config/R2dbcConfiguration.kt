package io.github.soat7.videomanager.database.r2dbc.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate

@Configuration
class R2dbcConfiguration {

    @Bean
    fun connectionFactory(databaseProperties: DatabaseProperties) =
        ConnectionFactoryResolver.resolve(databaseProperties)

    @Bean
    fun r2dbcEntityTemplate(connectionFactory: ConnectionFactory) = R2dbcEntityTemplate(connectionFactory)
}
