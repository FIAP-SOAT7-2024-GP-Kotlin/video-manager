package io.github.soat7.videomanager.webservice.controller.video

import io.github.soat7.videomanager.base.BaseIntegrationTest
import io.github.soat7.videomanager.fixture.VideoFixture
import io.github.soat7.videomanager.fixture.VideoFixture.uploadVideoRequest
import io.github.soat7.videomanager.webservice.api.VideoResponse
import io.kotest.common.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.http.MediaType

class VideoIT : BaseIntegrationTest() {

    @Nested
    inner class SuccessfulScenarios {

        @Test
        fun `Should successfully upload a Video`() {
            val request = uploadVideoRequest()
            val response = webTestClient.post()
                .uri("/api/v1/videos/upload")
                .header("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .is2xxSuccessful
                .expectBody(VideoResponse::class.java)
                .returnResult()
                .responseBody

            Assertions.assertNotNull(response)

            val storedVideo = runBlocking { videoRepository.findById(response!!.id) }
            Assertions.assertNotNull(storedVideo)
        }
    }

    @Test
    fun `Should successfully return video by user id`() {
        val video = createVideo(VideoFixture.createVideo())

        val response = webTestClient.get()
            .uri("/api/v1/videos/${video.userId}")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBodyList(VideoResponse::class.java)
            .returnResult()
            .responseBody

        Assertions.assertAll(
            Executable { Assertions.assertNotNull(response) },
            Executable { assertThat(response).isNotEmpty },
            Executable { Assertions.assertEquals(video.id, response!!.first().id) }
        )
    }
}
