package io.github.soat7.videomanager.database.r2dbc.config

import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import mu.KLogging
import mu.withLoggingContext

object ConnectionFactoryResolver {

    private val logger = KLogging().logger

    fun resolve(config: DatabaseProperties): ConnectionFactory {
        val builder = ConnectionFactoryOptions.parse(config.url).mutate()
            .option(ConnectionFactoryOptions.USER, config.username)
            .option(ConnectionFactoryOptions.PASSWORD, config.password)

        return if (config.pool.isEnabled) {
            val options = builder
                .option(ConnectionFactoryOptions.DRIVER, "pool")
                .option(ConnectionFactoryOptions.PROTOCOL, config.driver)
                .build()

            val factory = ConnectionFactories.get(options)

            withLoggingContext(
                "initial_size" to config.pool.initialSize.toString(),
                "max_size" to config.pool.maxSize.toString(),
                "max_life_time" to config.pool.maxLifeTime.toString()
            ) { logger.debug { "Creating pooled r2dbc connection" } }

            ConnectionPool(
                ConnectionPoolConfiguration.builder()
                    .connectionFactory(factory)
                    .initialSize(config.pool.initialSize)
                    .maxSize(config.pool.maxSize)
                    .maxLifeTime(config.pool.maxLifeTime)
                    .validationQuery(config.pool.validationQuery)
                    .build()
            )
        } else {
            logger.debug { "Creating common r2dbc connection" }
            ConnectionFactories.get(
                builder.option(ConnectionFactoryOptions.DRIVER, config.driver)
                    .build()
            )
        }
    }
}
