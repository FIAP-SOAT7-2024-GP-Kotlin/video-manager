package io.github.soat7.videomanager.webservice.controller.video

import io.github.soat7.videomanager.base.BaseIntegrationTest
import io.github.soat7.videomanager.fixture.VideoFixture.uploadVideoRequest
import io.github.soat7.videomanager.webservice.api.VideoResponse
import io.kotest.common.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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
}
