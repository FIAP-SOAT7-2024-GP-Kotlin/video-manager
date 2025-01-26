package io.github.soat7.videomanager.business.service

import io.github.soat7.videomanager.business.model.Video
import io.github.soat7.videomanager.business.repository.ObjectStorageRepository
import io.github.soat7.videomanager.business.repository.VideoRepository
import io.github.soat7.videomanager.webservice.api.UploadVideoRequest
import mu.KLogging
import java.util.UUID

class VideoService(
    private val objectStorageRepository: ObjectStorageRepository,
    private val repository: VideoRepository
) {

    private companion object : KLogging()

    suspend fun upload(request: UploadVideoRequest): Video {
        val data = objectStorageRepository.putObject("videos", request.content)

        return repository.create(Video.buildFrom(request, data))
    }

    suspend fun findByUserId(userId: UUID) = repository.findByUserId(userId)
}
