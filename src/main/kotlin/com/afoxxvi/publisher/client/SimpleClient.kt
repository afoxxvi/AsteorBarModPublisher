package com.afoxxvi.publisher.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.File

sealed class SimpleClient {
    companion object {
        fun getFileContentType(file: File): ContentType {
            return when (file.extension.lowercase()) {
                "jar" -> ContentType.parse("application/java-archive")
                "zip" -> ContentType.parse("application/zip")
                else -> ContentType.Application.OctetStream
            }
        }
    }

    protected val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }
    }
}