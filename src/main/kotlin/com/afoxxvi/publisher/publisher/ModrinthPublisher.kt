package com.afoxxvi.publisher.publisher

import com.afoxxvi.publisher.Config
import com.afoxxvi.publisher.client.ModrinthSimpleClient
import kotlinx.coroutines.delay
import java.io.File

object ModrinthPublisher {
    suspend fun publishModrinth(versionPairs: List<Pair<String, List<String>>>, loaders: List<String>) {
        if (!Config.publishToModrinth) {
            println("Modrinth publishing is disabled in the configuration.")
            return
        }
        val executeResults = mutableListOf<Pair<Boolean, String>>()
        ModrinthSimpleClient.readProperties()
        // Test if the token is valid
        ModrinthSimpleClient.getUser()
        versionPairs.forEach { (base, list) ->
            loaders.forEach { loader ->
                val path = Config.getFilePath(loader, base, Config.versionNumber)
                val file = File(path)
                if (!file.exists()) {
                    println("File does not exist: ${file.absolutePath}")
                    executeResults.add(false to "File does not exist: ${file.absolutePath}")
                    return@forEach
                }
                val result = ModrinthSimpleClient.createVersion(
                    Config.versionName,
                    Config.versionNumber,
                    list,
                    "release",
                    listOf(loader),
                    true,
                    "listed",
                    file
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