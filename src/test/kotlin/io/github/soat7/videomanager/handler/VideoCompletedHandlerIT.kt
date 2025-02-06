package io.github.soat7.videomanager.handler

import io.github.soat7.videomanager.base.BaseIntegrationTest
import io.github.soat7.videomanager.business.enum.VideoStatus
import io.github.soat7.videomanager.business.exception.ReasonCodeException
import io.github.soat7.videomanager.business.handler.VideoCompletedHandler
import io.github.soat7.videomanager.fixture.VideoFixture
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class VideoCompletedHandlerIT : BaseIntegrationTest() {

    @Autowired
    private lateinit var videoCompletedHandler: VideoCompletedHandler

    @Test
    fun `Should successfully handle video completed status`() = runTest {
        val video = createVideo(VideoFixture.createVideo())

        assertDoesNotThrow {
            videoCompletedHandler.handle(video)
        }

        videoRepository.findById(video.id)?.asClue {
            it.id shouldBe video.id
            it.metadata shouldBe video.metadata
            it.name shouldBe video.name
            it.status shouldBe VideoStatus.COMPLETED
        }
    }

    @Test
    fun `Should throw ReasonCodeException when video not found`() = runTest {
        val video = VideoFixture.createVideo()

        assertThrows<ReasonCodeException> {
            videoCompletedHandler.handle(video)
        }
    }
}
