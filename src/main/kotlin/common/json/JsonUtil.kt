package org.bank.common.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

object JsonUtil {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    fun <T> encodeToJson(value: T, serializer: KSerializer<T>): String {
        return json.encodeToString(serializer, value)
    }

    fun <T> decodeFromJson(value: String, serializer: KSerializer<T>): T {
        return json.decodeFromString(serializer, value)
    }
}
