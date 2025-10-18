package com.afoxxvi.publisher.publisher

import com.afoxxvi.publisher.Config
import com.afoxxvi.publisher.client.CurseForgeSimpleClient
import kotlinx.coroutines.delay

object CurseForgePublisher {
    suspend fun publishCurseForge(versionPairs: List<Pair<String, List<String>>>, loaders: List<String>) {
        if (!Config.publishToCurseForge) {
            println("CurseForge publishing is disabled in the configuration.")
            return
        }
        CurseForgeSimpleClient.readProperties()

        val executeResults = mutableListOf<Pair<Boolean, String>>()
        for ((base, list) in versionPairs) {
            for (loader in loaders) {
                val path = Config.getFilePath(loader, base, Config.versionNumber)
                val file = java.io.File(path)
                if (!file.exists()) {
                    println("File does not exist: ${file.absolutePath}")
                    executeResults.add(false to "File does not exist: ${file.absolutePath}")
                    continue
                }
                val result = CurseForgeSimpleClient.uploadFile(
                    Config.versionName,
                    list,
                    loader,
                    "release",
                    file,
                )
                val success = result == 200
                executeResults.add(success to "Uploading ${file.name} to versions ${list.joinToString(", ")}: ${if (success) "Success" else "Failed"}")
                // sleep between requests to avoid rate limiting
                delay(1000)
            }
        }
        println("=== Publish Results ===")
        println("Success: ${executeResults.count { it.first }} / ${executeResults.size}")
        executeResults.forEach { (_, message) ->
            println(message)
        }
    }
}