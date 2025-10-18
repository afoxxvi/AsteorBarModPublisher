package com.afoxxvi.publisher.dto.curse_forge

import kotlinx.serialization.Serializable

@Serializable
data class UploadFileRequest(
    val changelog: String,
    val changelogType: String, // text, html, markdown
    val displayName: String,
    var parentFileID: Int?,
    val gameVersions: List<Int>,
    val releaseType: String, // alpha, beta, release
    val isMarkedForManualRelease: Boolean?,
    val relations: Relations?,
) {
    @Serializable
    data class Relations(
        val projects: List<Project>,
    )

    @Serializable
    data class Project(
        val slug: String,
        val projectID: String,
        val type: String, // embeddedLibrary, incompatible, optionalDependency, requiredDependency, tool
    )
}