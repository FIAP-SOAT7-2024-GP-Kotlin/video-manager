package io.github.soat7.videomanager.webservice.common

import mu.KLogging
import mu.withLoggingContext
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class RequestServerLogger : WebFilter {

    companion object : KLogging()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val startMillis = System.currentTimeMillis()
        return chain.filter(exchange)
            .doOnSuccess {
                if (shouldSkipLog(exchange)) {
                    return@doOnSuccess
                }

                withLoggingContext(
                    "requester" to exchange.request.headers["x-ifood-requester-service"].toString(),
                    "method" to exchange.request.method.toString(),
                    "path" to exchange.request.path.toString(),
                    "params" to exchange.request.queryParams.toString(),
                    "status_code" to exchange.response.statusCode?.value().toString(),
                    "request_time_ms" to (System.currentTimeMillis() - startMillis).toString()
                ) { logger.debug { "Incoming request" } }
            }
            .doOnError {
                withLoggingContext(
                    "requester" to exchange.request.headers["x-ifood-requester-service"].toString(),
                    "method" to exchange.request.method.toString(),
                    "path" to exchange.request.path.toString(),
                    "params" to exchange.request.queryParams.toString(),
                    "status_code" to exchange.response.statusCode?.value().toString(),
                    "request_time_ms" to (System.currentTimeMillis() - startMillis).toString()
                ) { logger.warn { "Incoming request" } }
            }
    }

    private fun shouldSkipLog(exchange: ServerWebExchange): Boolean {
        return isPathActuator(exchange)
    }

    private fun isPathActuator(exchange: ServerWebExchange) =
        exchange.request.path.toString().contains("/actuator")
}
