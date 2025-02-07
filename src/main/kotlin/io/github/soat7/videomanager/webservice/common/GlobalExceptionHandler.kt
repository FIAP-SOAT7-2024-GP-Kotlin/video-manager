package io.github.soat7.videomanager.webservice.common

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.soat7.videomanager.business.exception.ReasonCodeException
import io.github.soat7.videomanager.business.exception.enum.ReasonCode
import io.github.soat7.videomanager.webservice.api.ErrorResponse
import mu.KLogging
import mu.withLoggingContext
import org.springframework.core.annotation.Order
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

@Component
@Order(-2)
internal class GlobalExceptionHandler(
    private val objectMapper: ObjectMapper
) : WebExceptionHandler {
    companion object : KLogging()

    internal data class HttpError(
        val httpStatus: HttpStatusCode,
        val code: String,
        val messages: List<String>
    ) {
        constructor(reasonCode: ReasonCode, additionalInformation: List<String> = emptyList()) : this(
            reasonCode.status,
            reasonCode.code,
            listOf(reasonCode.description) + additionalInformation
        )
    }

    override fun handle(exchange: ServerWebExchange, t: Throwable): Mono<Void> {
        val error = when (t) {
            is ReasonCodeException -> HttpError(t.reasonCode, t.additionalInformation)
            is ResponseStatusException -> HttpError(t.statusCode, ReasonCode.UNEXPECTED_ERROR.code, listOf(t.message))
            else -> HttpError(ReasonCode.UNEXPECTED_ERROR)
        }
        log(t, error)

        val buffer: DataBuffer = try {
            val bytes = ErrorResponse(error.code, error.messages)
                .let { objectMapper.writeValueAsBytes(it) }

            exchange.response.bufferFactory().wrap(bytes)
        } catch (e: JsonProcessingException) {
            exchange.response.bufferFactory().wrap("".toByteArray())
        }

        exchange.response.headers.contentType = MediaType.APPLICATION_JSON
        exchange.response.statusCode = error.httpStatus

        return exchange.response.writeWith(Mono.just(buffer))
    }

    private fun log(t: Throwable, error: HttpError) {
        if (error.httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
            logger.error(t) { "Internal Server error | message: [${t.message}]" }
        } else {
            logger.warn(t) { "Unexpected error | message: [${t.message}]" }
        }
    }
}
