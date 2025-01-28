package io.github.soat7.videomanager.webservice.api

import org.springframework.http.codec.multipart.FilePart
import java.util.UUID

data class UploadVideoRequest(
    val userId: UUID,
    val name: String,
    val content: FilePart
)
