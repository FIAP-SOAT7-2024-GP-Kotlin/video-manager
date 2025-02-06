package io.github.soat7.videomanager.handler

import io.github.soat7.videomanager.base.BaseIntegrationTest
import io.github.soat7.videomanager.business.enum.VideoStatus
import io.github.soat7.videomanager.business.exception.ReasonCodeException
import io.github.soat7.videomanager.business.handler.VideoErrorHandler
import io.github.soat7.videomanager.fixture.VideoFixture
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class VideoErrorHandlerIT : BaseIntegrationTest() {

    @Autowired
    private lateinit var videoErrorHandler: VideoErrorHandler

    @Test
    fun `Should successfully handle video error status`() = runTest {
        val video = createVideo(VideoFixture.createVideo())

        assertDoesNotThrow {
            videoErrorHandler.handle(video)
        }

        videoRepository.findById(video.id)?.asClue {
            it.id shouldBe video.id
            it.metadata shouldBe video.metadata
            it.name shouldBe video.name
            it.status shouldBe VideoStatus.ERROR
        }
    }

    @Test
    fun `Should throw ReasonCodeException when video not found`() = runTest {
        val video = VideoFixture.createVideo()

        assertThrows<ReasonCodeException> {
            videoErrorHandler.handle(video)
        }
    }
}
