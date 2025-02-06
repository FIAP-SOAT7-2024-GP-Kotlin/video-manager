package io.github.soat7.videomanager.base

import io.github.soat7.videomanager.Application
import io.github.soat7.videomanager.business.model.Video
import io.github.soat7.videomanager.business.repository.VideoRepository
import io.github.soat7.videomanager.container.PostgresContainer
import io.kotest.common.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.await
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("test")
@SpringBootTest(
    classes = [Application::class],
    webEnvironment = WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient(timeout = "PT10000000S")
@ExtendWith(PostgresContainer::class)
class BaseIntegrationTest {

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @Autowired
    protected lateinit var videoRepository: VideoRepository

    @Autowired
    private lateinit var dbClient: DatabaseClient

    @BeforeEach
    fun setup() = runBlocking {
        dbClient.sql("DELETE FROM video_manager.video").await()
    }

    protected fun createVideo(video: Video) = runBlocking {
        videoRepository.create(video)
    }
}
