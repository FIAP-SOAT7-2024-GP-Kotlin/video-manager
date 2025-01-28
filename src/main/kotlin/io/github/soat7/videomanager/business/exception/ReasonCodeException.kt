package io.github.soat7.videomanager.business.exception

import io.github.soat7.videomanager.business.exception.enum.ReasonCode

class ReasonCodeException(
    val reasonCode: ReasonCode,
    cause: Throwable? = null,
    val additionalInformation: List<String> = emptyList()
) : RuntimeException(reasonCode.description, cause)
