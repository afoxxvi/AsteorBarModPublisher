package com.afoxxvi.publisher.dto.modrinth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateVersionRequest(
    val name: String,
    @SerialName("version_number")
    val versionNumber: String,
    val changelog: String,
    val dependencies: List<Dependency>,
    @SerialName("game_versions")
    val gameVersions: List<String>,
    @SerialName("version_type")
    val versionType: String,
    val loaders: List<String>,
    val featured: Boolean,
    val status: String,
    @SerialName("requested_status")
    val requestedStatus: String?,
    @SerialName("project_id")
    val projectId: String,
    @SerialName("file_parts")
    val fileParts: List<String>,
    @SerialName("primary_file")
    val primaryFile: String,
) {
    @Serializable
    class Dependency(
        @SerialName("version_id")
        val versionId: String?,
        @SerialName("project_id")
        val projectId: String?,
        @SerialName("file_name")
        val fileName: String?,
        @SerialName("dependency_type")
        val dependencyType: String,
    )
}