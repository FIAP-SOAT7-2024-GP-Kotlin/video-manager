package io.github.soat7.videomanager.business.repository

import io.github.soat7.videomanager.business.model.Video

interface VideoRepository {

    suspend fun create(video: Video)

    suspend fun findById(id: String): Video?

    suspend fun findByUserId(userId: String): List<Video>
}
