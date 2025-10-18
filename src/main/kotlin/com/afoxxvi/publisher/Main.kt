package com.afoxxvi.publisher

import com.afoxxvi.publisher.publisher.CurseForgePublisher
import com.afoxxvi.publisher.publisher.ModrinthPublisher

val loaders = listOf("Forge", "Fabric", "NeoForge")

// Base version to All compatible versions
val versionPairs = listOf(
    "1.20.1" to listOf("1.20.1"),
    "1.20.4" to listOf("1.20.4"),
    "1.20.6" to listOf("1.20.6"),
    "1.21.1" to listOf("1.21.1"),
    "1.21.3" to listOf("1.21.3"),
    "1.21.4" to listOf("1.21.4"),
    "1.21.5" to listOf("1.21.5"),
    "1.21.6" to listOf("1.21.6", "1.21.7", "1.21.8"),
    "1.21.9" to listOf("1.21.9", "1.21.10"),
)

suspend fun main() {
    Config.initializePublisher()
    ModrinthPublisher.publishModrinth(versionPairs, loaders)
    CurseForgePublisher.publishCurseForge(versionPairs, loaders)
}
