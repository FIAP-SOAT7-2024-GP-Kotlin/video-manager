package io.github.soat7.videomanager.business.service

import io.github.soat7.videomanager.business.model.Video
import io.github.soat7.videomanager.business.publisher.VideoPublisher
import io.github.soat7.videomanager.business.repository.ObjectStorageRepository
import io.github.soat7.videomanager.business.repository.VideoRepository
import io.github.soat7.videomanager.webservice.api.UploadVideoRequest
import mu.KLogging
import java.util.UUID

class VideoService(
    private val objectStorageRepository: ObjectStorageRepository,
    private val repository: VideoRepository,
    private val videoPublisher: VideoPublisher
) {

    private companion object : KLogging()

    suspend fun upload(request: UploadVideoRequest): Video {
        val data = objectStorageRepository.putObject(request.content)

        return repository.create(Video.buildFrom(request, data)).also {
            videoPublisher.sendVideoCreated(it)
        }
    }

    suspend fun findById(id: UUID): Video? = repository.findById(id)

    suspend fun findByUserId(userId: UUID) = repository.findByUserId(userId)

    suspend fun update(video: Video) = repository.update(video)
}
