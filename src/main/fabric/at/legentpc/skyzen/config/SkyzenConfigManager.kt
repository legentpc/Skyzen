package at.legentpc.skyzen.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException

class SkyzenConfigManager {

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val logger = LoggerFactory.getLogger(SkyzenConfigManager::class.java)

    val configFile: File = FabricLoader.getInstance()
        .configDir.resolve("skyzen/skyzen.json").toFile()

    val config: SkyzenConfig = loadConfig()

    // A missing or corrupt file falls back to defaults and rewrites itself.
    private fun loadConfig(): SkyzenConfig {
        if (configFile.exists()) {
            try {
                gson.fromJson(configFile.readText(), SkyzenConfig::class.java)?.let { return it }
            } catch (e: IOException) {
                logger.error("Failed to read Skyzen config from $configFile.", e)
            } catch (e: JsonParseException) {
                logger.error("Failed to parse Skyzen config from $configFile.", e)
            }
        }
        val fresh = SkyzenConfig()
        write(fresh)
        return fresh
    }

    fun saveConfig() = write(config)

    private fun write(value: SkyzenConfig) {
        try {
            configFile.parentFile.mkdirs()
            configFile.writeText(gson.toJson(value))
        } catch (e: IOException) {
            logger.error("Failed to write Skyzen config to $configFile.", e)
        } catch (e: JsonIOException) {
            logger.error("Failed to serialize Skyzen config to $configFile.", e)
        }
    }
}
