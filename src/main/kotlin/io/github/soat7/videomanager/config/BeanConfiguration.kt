package io.github.soat7.videomanager.config

import io.github.soat7.videomanager.business.publisher.VideoPublisher
import io.github.soat7.videomanager.business.repository.ObjectStorageRepository
import io.github.soat7.videomanager.business.repository.VideoRepository
import io.github.soat7.videomanager.business.service.VideoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun videoService(
        objectStorageRepository: ObjectStorageRepository,
        videoRepository: VideoRepository,
        videoPublisher: VideoPublisher
    ): VideoService = VideoService(
        objectStorageRepository = objectStorageRepository,
        repository = videoRepository,
        videoPublisher = videoPublisher
    )
}
