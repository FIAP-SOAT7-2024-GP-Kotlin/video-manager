package io.github.soat7.videomanager.business.handler

import io.github.soat7.videomanager.business.enum.VideoStatus
import io.github.soat7.videomanager.business.exception.ReasonCodeException
import io.github.soat7.videomanager.business.exception.enum.ReasonCode
import io.github.soat7.videomanager.business.model.Video
import io.github.soat7.videomanager.business.service.VideoService
import mu.KLogging
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class VideoCompletedHandler(
    private val videoService: VideoService
) {

    private companion object : KLogging()

    suspend fun handle(video: Video) {
        logger.info { "Handling video completed status: ${video.id}" }
        videoService.findById(video.id)?.let {
            videoService.update(
                it.copy(
                    status = VideoStatus.COMPLETED,
                    outputPath = video.outputPath,
                    metadata = video.metadata,
                    updatedAt = Instant.now()
                )
            )
        } ?: throw ReasonCodeException(ReasonCode.VIDEO_NOT_FOUND)
    }
}
