package io.github.soat7.videomanager.thirdparty

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import io.github.soat7.videomanager.business.exception.ReasonCodeException
import io.github.soat7.videomanager.business.exception.enum.ReasonCode
import io.github.soat7.videomanager.business.model.FileContent
import io.github.soat7.videomanager.business.repository.ObjectStorageRepository
import kotlinx.coroutines.reactor.awaitSingle
import mu.KLogging
import org.apache.commons.io.FilenameUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class ObjectStorageClient(
    @Value("\${digital-ocean.spaces.bucket.name}") private val bucketName: String,
    @Value("\${digital-ocean.spaces.bucket.path}") private val path: String,
    private val amazonS3: AmazonS3
) : ObjectStorageRepository {

    private companion object : KLogging()

    override suspend fun putObject(file: FilePart): FileContent {
        return try {
            val key = buildKey(file)

            amazonS3.putObject(bucketName, key, file.toInputStream(), file.buildObjectMetadata())

            logger.info { "File uploaded to Object Storage" }

            val s3Object = amazonS3.getObject(GetObjectRequest(bucketName, key))
            FileContent(
                path = s3Object.key,
                metadata = s3Object.objectMetadata.rawMetadata
            )
        } catch (ex: Exception) {
            logger.error(ex) { "An error occurred while uploading file to Object Storage due to: ${ex.message}" }
            throw ReasonCodeException(ReasonCode.UNEXPECTED_ERROR, ex)
        }
    }

    private fun buildKey(file: FilePart) =
        "$path/${FilenameUtils.removeExtension(file.filename())}.${FilenameUtils.getExtension(file.filename())}"

    private suspend fun FilePart.toInputStream(): InputStream =
        DataBufferUtils.join(this.content())
            .map { it.asInputStream() }
            .awaitSingle() ?: throw IllegalArgumentException("Invalid file")

    private suspend fun FilePart.buildObjectMetadata(): ObjectMetadata {
        val inputStream = DataBufferUtils.join(this.content())
            .map { it.asInputStream() }
            .awaitSingle() ?: throw IllegalArgumentException("Invalid file")

        return ObjectMetadata().apply {
            contentType = this@buildObjectMetadata.headers().contentType.toString().takeIf { it.isNotEmpty() }
            contentLength = inputStream.available().toLong()
        }
    }
}
