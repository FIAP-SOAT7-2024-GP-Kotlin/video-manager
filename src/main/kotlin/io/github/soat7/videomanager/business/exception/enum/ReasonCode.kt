package io.github.soat7.videomanager.business.exception.enum

import org.springframework.http.HttpStatus

enum class ReasonCode(
    val status: HttpStatus,
    val code: String,
    val description: String
) {
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0001", "An unexpected error occurred"),
    UNEXPECTED_INTEGRATION_ERROR(HttpStatus.BAD_GATEWAY, "0002", "An unexpected integration error occurred"),

    INVALID_UUID(HttpStatus.BAD_REQUEST, "0020", "Invalid uuid"),

    VIDEO_NOT_FOUND(HttpStatus.NOT_FOUND, "0040", "Video not found")
}
