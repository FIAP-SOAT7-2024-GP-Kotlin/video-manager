package io.github.soat7.videomanager.webservice.api

data class ErrorResponse(
    val code: String,
    val messages: List<String>
)
