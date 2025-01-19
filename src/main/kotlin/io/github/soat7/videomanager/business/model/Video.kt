package io.github.soat7.videomanager.business.model

import io.github.soat7.videomanager.business.enum.VideoStatus
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
)
