package io.github.soat7.videomanager.business.model

import io.github.soat7.videomanager.business.enum.VideoStatus
import io.github.soat7.videomanager.webservice.api.UploadVideoRequest
import java.time.Instant
import java.util.UUID

data class Video(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val status: VideoStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
    val metadata: Map<String, Any>? = emptyMap(),
    val inputPath: String,
    val outputPath: String? = null
) {
    companion object {
        fun buildFrom(uploadVideoRequest: UploadVideoRequest, fileContent: FileContent) = Video(
            id = UUID.randomUUID(),
            userId = uploadVideoRequest.userId,
            name = uploadVideoRequest.name,
            status = VideoStatus.PENDING,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            metadata = fileContent.metadata,
            inputPath = fileContent.path
        )
    }
}
