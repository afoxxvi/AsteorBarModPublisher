package com.afoxxvi.publisher.client

import com.afoxxvi.publisher.dto.curse_forge.UploadFileRequest
import com.afoxxvi.publisher.utils.Root
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headersOf
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

object CurseForgeSimpleClient : SimpleClient() {
    private val versionMapped = mutableMapOf<String, Int>()

    var authToken = ""
    var projectId = ""

    fun readProperties() {
        val props = Properties()
        val stream = ModrinthSimpleClient::class.java.classLoader.getResourceAsStream("publish.properties") ?: return
        stream.use { props.load(it) }
        authToken = props.getProperty("curse-forge-auth-token", authToken)
        projectId = props.getProperty("curse-forge-project-id", projectId)
        println("Project ID: $projectId")
        val stream2 = ModrinthSimpleClient::class.java.classLoader.getResourceAsStream("curseforge_versions_data.json") ?: return
        stream2.use {
            val jsonString = it.reader().readText()
            val json = Json { ignoreUnknownKeys = true }
            val versionsData = json.decodeFromString(
                Root.serializer(),
                jsonString
            )
            versionsData.versionsData.flatten().forEach { data ->
                data.choices.forEach { choice ->
                    versionMapped[choice.name.lowercase()] = choice.id
                }
            }
        }
    }

    private fun getUrl(): String {
        return "https://minecraft.curseforge.com"
    }

    suspend fun uploadFile(
        version: String,
        gameVersions: List<String>,
        loader: String,
        releaseType: String,
        file: File,
    ): Int {
        val url = getUrl() + "/api/projects/$projectId/upload-file"
        val gameVersions = gameVersions.mapNotNull {
            versionMapped[it.lowercase()]
        } + listOfNotNull(
            versionMapped[loader.lowercase()]
        )
        val request = UploadFileRequest(
            changelog = "",
            changelogType = "text",
            displayName = "$version $loader",
            parentFileID = null,
            gameVersions = gameVersions,
            releaseType = releaseType,
            isMarkedForManualRelease = false,
            relations = null,
        )
        val json = Json { explicitNulls = false }
        val response = client.post(url) {
            header("X-Api-Token", authToken)
            contentType(ContentType.MultiPart.FormData)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            "metadata",
                            json.encodeToString(
                                request
                            ),
                            headersOf(
                                "Content-Type",
                                "application/json"
                            )
                        )
                        append(
                            "file",
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
        println("Upload file response: $response")
        val responseBody = response.body<String>()
        println("Response body: $responseBody")
        return response.status.value
    }
}