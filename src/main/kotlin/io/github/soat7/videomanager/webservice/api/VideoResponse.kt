package io.github.soat7.videomanager.webservice.api

import io.github.soat7.videomanager.business.model.Video
import java.util.UUID

data class VideoResponse(
    val id: UUID
) {
    companion object {
        fun from(video: Video) = VideoResponse(
            id = video.id
        )
    }
}
