package io.github.soat7.videomanager.business.publisher

import io.github.soat7.videomanager.business.model.Video

interface VideoPublisher {

    fun sendVideoCreated(video: Video)
}
