package io.github.soat7.videomanager.database.r2dbc.extensions

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.r2dbc.postgresql.codec.Json
import io.r2dbc.spi.Row

inline fun <reified T> Row.getTyped(field: String): T = this.get(field, T::class.java)!!

inline fun <reified T> Row.getTypedOrNull(field: String): T? = this.get(field, T::class.java)

inline fun <reified T : Enum<T>> Row.getEnum(field: String): T = enumValueOf(this.getTyped(field))

fun Row.getMapFromJson(field: String): Map<String, Any>? {
    val json = this.get(field, Json::class.java)
    return json?.let { jacksonObjectMapper().readValue(json.asString(), object : TypeReference<Map<String, Any>>() {}) }
}
