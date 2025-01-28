package io.github.soat7.videomanager.business.repository

import io.github.soat7.videomanager.business.model.FileContent
import org.springframework.http.codec.multipart.FilePart

interface ObjectStorageRepository {

    suspend fun putObject(path: String, file: FilePart): FileContent
}
