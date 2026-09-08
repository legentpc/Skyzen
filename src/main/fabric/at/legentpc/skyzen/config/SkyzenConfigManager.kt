package at.legentpc.skyzen.config

import io.github.notenoughupdates.moulconfig.managed.ManagedConfig
import net.fabricmc.loader.api.FabricLoader
import java.io.File

class SkyzenConfigManager {

    val managedConfig: ManagedConfig<SkyzenConfig> = ManagedConfig.create(
        File(FabricLoader.getInstance().configDir.toFile(), "skyzen/skyzen.json"),
        SkyzenConfig::class.java
    )

    val config: SkyzenConfig get() = managedConfig.instance

    fun saveConfig() {
        managedConfig.saveToFile()
    }
}
