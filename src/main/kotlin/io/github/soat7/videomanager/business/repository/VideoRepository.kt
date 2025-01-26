package io.github.soat7.videomanager.business.repository

import io.github.soat7.videomanager.business.model.Video
import java.util.UUID

interface VideoRepository {

    suspend fun create(video: Video): Video

    suspend fun findById(id: UUID): Video?

    suspend fun findByUserId(userId: UUID): List<Video>
}
