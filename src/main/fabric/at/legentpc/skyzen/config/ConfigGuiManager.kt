package at.legentpc.skyzen.config

import at.legentpc.skyzen.SkyzenModLoader
import io.github.notenoughupdates.moulconfig.common.IMinecraft
import net.minecraft.client.Minecraft

object ConfigGuiManager {

    fun openGui() {
        val client = Minecraft.getInstance()
        client.execute {
            val editor = SkyzenModLoader.configManager.managedConfig.getEditor()
            IMinecraft.INSTANCE.openWrappedScreen(editor)
        }
    }
}
