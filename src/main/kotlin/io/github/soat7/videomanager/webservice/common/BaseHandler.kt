package io.github.soat7.videomanager.webservice.common

import io.github.soat7.videomanager.business.exception.ReasonCodeException
import io.github.soat7.videomanager.business.exception.enum.ReasonCode
import org.springframework.web.reactive.function.server.ServerRequest
import java.util.UUID

abstract class BaseHandler {

    companion object {
        const val PATH_VARIABLE_OPERATION_ID = "user_id"
    }

    fun ServerRequest.pathVariableAsUuid(pathVariable: String): UUID {
        return this.pathVariable(pathVariable).let {
            try {
                UUID.fromString(it)
            } catch (e: Exception) {
                throw ReasonCodeException(ReasonCode.INVALID_UUID)
            }
        }
    }
}
