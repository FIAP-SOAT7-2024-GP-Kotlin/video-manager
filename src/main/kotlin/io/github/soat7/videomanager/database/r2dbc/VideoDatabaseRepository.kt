package io.github.soat7.videomanager.database.r2dbc

import io.github.soat7.videomanager.business.model.Video
import io.github.soat7.videomanager.business.repository.VideoRepository
import io.github.soat7.videomanager.database.r2dbc.extensions.bindEnum
import io.github.soat7.videomanager.database.r2dbc.extensions.bindMapToJson
import io.github.soat7.videomanager.database.r2dbc.extensions.bindOrNull
import io.github.soat7.videomanager.database.r2dbc.extensions.getEnum
import io.github.soat7.videomanager.database.r2dbc.extensions.getMapFromJson
import io.github.soat7.videomanager.database.r2dbc.extensions.getTyped
import io.github.soat7.videomanager.database.r2dbc.extensions.getTypedOrNull
import io.github.soat7.videomanager.database.r2dbc.statements.VideoSqlStatements
import io.r2dbc.spi.Row
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class VideoDatabaseRepository(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
) : VideoRepository {

    private val dbClient = r2dbcEntityTemplate.databaseClient

    override suspend fun create(video: Video): Video = dbClient.sql(VideoSqlStatements.INSERT)
        .bind("id", video.id)
        .bind("name", video.name)
        .bind("user_id", video.userId)
        .bindEnum("status", video.status)
        .bind("created_at", video.createdAt)
        .bind("updated_at", video.updatedAt)
        .bindMapToJson("metadata", video.metadata)
        .bind("input_path", video.inputPath)
        .bindOrNull("output_path", video.outputPath)
        .then()
        .thenReturn(video)
        .awaitSingle()

    override suspend fun update(video: Video): Video = dbClient.sql(VideoSqlStatements.UPDATE)
        .bind("id", video.id)
        .bindEnum("status", video.status)
        .bind("updated_at", video.updatedAt)
        .bindMapToJson("metadata", video.metadata)
        .bindOrNull("output_path", video.outputPath)
        .then()
        .thenReturn(video)
        .awaitSingle()

    override suspend fun findById(id: UUID): Video? =
        dbClient.sql(VideoSqlStatements.FIND_BY_ID)
            .bind("id", id)
            .map { row, _ -> row.toVideo() }
            .awaitSingleOrNull()

    override suspend fun findByUserId(userId: UUID): List<Video> =
        dbClient.sql(VideoSqlStatements.FIND_BY_USER_ID)
            .bind("user_id", userId)
            .map { row, _ -> row.toVideo() }
            .all()
            .collectList()
            .awaitSingle()

    private fun Row.toVideo() = Video(
        id = getTyped("id"),
        userId = getTyped("user_id"),
        name = getTyped("name"),
        status = getEnum("status"),
        createdAt = getTyped("created_at"),
        updatedAt = getTyped("updated_at"),
        metadata = getMapFromJson("metadata"),
        inputPath = getTyped("input_path"),
        outputPath = getTypedOrNull("output_path")
    )
}
