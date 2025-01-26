package io.github.soat7.videomanager.webservice.controller.video

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class VideoRouter {

    private companion object {
        const val PATH = "/api/v1/videos"
    }

    @Bean
    fun videoRoutes(handler: VideoHandler) = coRouter {
        PATH.nest {
            POST("/upload", handler::upload)
            GET("/{user_id}", handler::findByUserId)
        }
    }
}
