package at.legentpc.skyzen.config

import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import java.io.File

class SkyzenConfigManager {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    val configFile: File = FabricLoader.getInstance()
        .configDir.resolve("skyzen/skyzen.json").toFile()

    val config: SkyzenConfig = loadConfig()

    // A missing or corrupt file falls back to defaults and rewrites itself.
    private fun loadConfig(): SkyzenConfig {
        if (configFile.exists()) {
            try {
                gson.fromJson(configFile.readText(), SkyzenConfig::class.java)?.let { return it }
            } catch (e: Exception) {
            }
        }
        val fresh = SkyzenConfig()
        write(fresh)
        return fresh
    }

    fun saveConfig() = write(config)

    private fun write(value: SkyzenConfig) {
        configFile.parentFile.mkdirs()
        configFile.writeText(gson.toJson(value))
    }
}
