package com.afoxxvi.publisher.utils

import kotlinx.serialization.Serializable

@Serializable
data class Root(
    val versionsData: List<List<VersionsData>>,
)

@Serializable
data class VersionsData(
    val name: String,
    val validationGroupName: String,
    val isRequired: Boolean,
    val selectionMode: Int,
    val choices: List<Choice>,
)

@Serializable
data class Choice(
    val id: Int,
    val name: String,
)