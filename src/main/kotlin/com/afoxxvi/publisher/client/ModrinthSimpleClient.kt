package com.afoxxvi.publisher.client

import com.afoxxvi.publisher.dto.modrinth.CreateVersionRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

object ModrinthSimpleClient : SimpleClient() {
    const val TEST_URL = "https://staging-api.modrinth.com/v2"
    const val API_URL = "https://api.modrinth.com/v2"

    const val PRODUCTION = true

    private fun getUrl(): String {
        return if (PRODUCTION) API_URL else TEST_URL
    }

    var authToken = ""
    var projectId = ""

    fun readProperties() {
        val props = Properties()
        val stream = ModrinthSimpleClient::class.java.classLoader.getResourceAsStream("publish.properties") ?: return
        stream.use { props.load(it) }
        authToken = props.getProperty("modrinth-auth-token", authToken)
        projectId = props.getProperty("modrinth-project-id", projectId)
        println("Project ID: $projectId")
    }

    suspend fun getUser(
    ) {
        val url = getUrl() + "/user"
        val response = client.get(url) {
            header(HttpHeaders.Authorization, authToken)
        }
        val responseBody = response.body<String>()
        println("Get user response: $responseBody")
    }

    /**
     * This route creates a version on an existing project. There must be at least one file attached to each new version, unless the new version’s status is draft. .mrpack, .jar, .zip, and .litemod files are accepted.
     *
     * The request is a multipart request with at least two form fields: one is data, which includes a JSON body with the version metadata as shown below, and at least one field containing an upload file.
     *
     * You can name the file parts anything you would like, but you must list each of the parts’ names in file_parts, and optionally, provide one to use as the primary file in primary_file.
     */
    suspend fun createVersion(
        version: String,
        versionNumber: String,
        gameVersions: List<String>,
        versionType: String,
        loaders: List<String>,
        featured: Boolean,
        status: String,
        file: File,
    ): Int {
        val url = getUrl() + "/version"
        val request = CreateVersionRequest(
            name = version,
            versionNumber = versionNumber,
            changelog = "",
            dependencies = emptyList(),
            gameVersions = gameVersions,
            versionType = versionType,
            loaders = loaders,
            featured = featured,
            status = status,
            requestedStatus = null,
            projectId = projectId,
            fileParts = listOf(file.name),
            primaryFile = file.name,
        )
        val response = client.post(url) {
            header(HttpHeaders.Authorization, authToken)
            contentType(ContentType.MultiPart.FormData)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            "data",
                            Json.encodeToString(
                                request
                            ),
                            headersOf(
                                "Content-Type",
                                "application/json"
                            )
                        )
                        append(
                            file.name,
                            file.readBytes(),
                            headersOf(
                                HttpHeaders.ContentDisposition to listOf("form-data; name=\"${file.name}\"; filename=\"${file.name}\""),
                                HttpHeaders.ContentType to listOf(getFileContentType(file).toString())
                            )
                        )
                    }
                )
            )
        }
        println("Create version response: $response")
        val responseBody = response.body<String>()
        println("Response body: $responseBody")
        return response.status.value
    }
}