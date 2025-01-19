package io.github.soat7.videomanager.database.r2dbc.extensions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.r2dbc.postgresql.codec.Json
import org.springframework.r2dbc.core.DatabaseClient

inline fun <reified T : Any> DatabaseClient.GenericExecuteSpec.bindOrNull(name: String, value: T?) =
    value?.let { this.bind(name, it as Any) } ?: this.bindNull(name, T::class.java)

inline fun <reified T : Enum<T>> DatabaseClient.GenericExecuteSpec.bindEnum(name: String, value: T) =
    bind(name, value.name)

fun DatabaseClient.GenericExecuteSpec.bindMapToJson(name: String, value: Map<String, Any>?) =
    value?.let { this.bind(name, Json.of(jacksonObjectMapper().writeValueAsString(value))) }
        ?: this.bindNull(name, Json::class.java)
