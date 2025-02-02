package io.github.soat7.videomanager.messaging.producer

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.soat7.videomanager.business.model.Video
import io.github.soat7.videomanager.business.publisher.VideoPublisher
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import io.nats.client.Connection as NatsConnection

@Component
class VideoPendingProducer(
    @Value("\${messaging.topics.video-pending}") private val topic: String,
    private val natsConnection: NatsConnection,
    private val objectMapper: ObjectMapper
) : VideoPublisher {

    private companion object : KLogging()

    override fun sendVideoCreated(video: Video) {
        logger.info { "Sending video created message: ${video.id}" }
        natsConnection.publish(topic, objectMapper.writeValueAsBytes(video))
    }
}
