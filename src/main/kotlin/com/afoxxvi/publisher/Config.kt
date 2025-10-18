package com.afoxxvi.publisher

import java.util.Properties

object Config {
    var buildRoot = ""
    var forgeRoot = ""
    var fabricRoot = ""
    var neoforgeRoot = ""
    var forgeFileFormat = ""
    var fabricFileFormat = ""
    var neoforgeFileFormat = ""
    var versionName = ""
    var versionNumber = ""
    var publishToModrinth = false
    var publishToCurseForge = false

    fun readProperties() {
        val props = Properties()
        val stream = Config::class.java.classLoader.getResourceAsStream("publish.properties") ?: return
        stream.use { props.load(it) }
        buildRoot = props.getProperty("build-root", buildRoot)
        val forgePath = props.getProperty("forge-path", "")
        val fabricPath = props.getProperty("fabric-path", "")
        val neoforgePath = props.getProperty("neoforge-path", "")
        forgeRoot = "$buildRoot/$forgePath"
        fabricRoot = "$buildRoot/$fabricPath"
        neoforgeRoot = "$buildRoot/$neoforgePath"
        forgeFileFormat = props.getProperty("forge-file-format", forgeFileFormat)
        fabricFileFormat = props.getProperty("fabric-file-format", fabricFileFormat)
        neoforgeFileFormat = props.getProperty("neoforge-file-format", neoforgeFileFormat)
        versionName = props.getProperty("version-name", versionName)
        versionNumber = props.getProperty("version-number", versionNumber)
        publishToModrinth = props.getProperty("publish-to-modrinth", "false").toBoolean()
        publishToCurseForge = props.getProperty("publish-to-curse-forge", "false").toBoolean()
    }

    fun allPresent(): Boolean {
        return buildRoot.isNotEmpty() &&
            forgeRoot.isNotEmpty() &&
            fabricRoot.isNotEmpty() &&
            neoforgeRoot.isNotEmpty() &&
            forgeFileFormat.isNotEmpty() &&
            fabricFileFormat.isNotEmpty() &&
            neoforgeFileFormat.isNotEmpty() &&
            versionName.isNotEmpty() &&
            versionNumber.isNotEmpty()
    }

    fun getFilePath(loader: String, base: String, versionNumber: String): String {
        return when (loader.lowercase()) {
            "forge" -> "$forgeRoot/${String.format(forgeFileFormat, base, versionNumber)}"
            "fabric" -> "$fabricRoot/${String.format(fabricFileFormat, base, versionNumber)}"
            "neoforge" -> "$neoforgeRoot/${String.format(neoforgeFileFormat, base, versionNumber)}"
            else -> throw IllegalArgumentException("Unknown loader: $loader")
        }
    }

    fun initializePublisher() {
        readProperties()
        require(allPresent()) {
            "Configuration is incomplete. Please check publish.properties."
        }
    }
}