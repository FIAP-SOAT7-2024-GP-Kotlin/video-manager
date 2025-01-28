package io.github.soat7.videomanager.webservice.controller.video

import io.github.soat7.videomanager.business.exception.ReasonCodeException
import io.github.soat7.videomanager.business.exception.enum.ReasonCode
import io.github.soat7.videomanager.business.service.VideoService
import io.github.soat7.videomanager.webservice.api.UploadVideoRequest
import io.github.soat7.videomanager.webservice.api.VideoResponse
import io.github.soat7.videomanager.webservice.common.BaseHandler
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.FormFieldPart
import org.springframework.http.codec.multipart.Part
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitMultipartData
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.util.UUID

@Component
class VideoHandler(
    private val videoService: VideoService
) : BaseHandler() {

    suspend fun upload(request: ServerRequest): ServerResponse {
        val uploadVideoRequest = extractFormDataUploadVideoRequest(request)
        val video = videoService.upload(uploadVideoRequest)
        return ServerResponse.ok().bodyValueAndAwait(VideoResponse.from(video))
    }

    suspend fun findByUserId(request: ServerRequest): ServerResponse {
        val userId = request.pathVariableAsUuid(PATH_VARIABLE_OPERATION_ID)
        val videos = videoService.findByUserId(userId)
        return ServerResponse.ok().bodyValueAndAwait(videos.map { VideoResponse.from(it) })
    }

    private suspend fun extractFormDataUploadVideoRequest(request: ServerRequest): UploadVideoRequest {
        val multiPart = request.awaitMultipartData()
        val partMap: Map<String, Part> = multiPart.toSingleValueMap()

        return UploadVideoRequest(
            userId = partMap["user_id"]?.let { UUID.fromString((it as FormFieldPart).value()) }
                ?: throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR),
            name = partMap["name"]?.let { (it as FormFieldPart).value() }
                ?: throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR),
            content = partMap["content"] as? FilePart
                ?: throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR)
        )
    }
}
