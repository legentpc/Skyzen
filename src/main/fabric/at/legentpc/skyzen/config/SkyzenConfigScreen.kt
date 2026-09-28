package at.legentpc.skyzen.config

import at.legentpc.skyzen.SkyzenModLoader
import com.moulberry.lattice.Lattice
import com.moulberry.lattice.element.LatticeElements
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

object SkyzenConfigScreen {

    private fun createElements(): LatticeElements {
        return LatticeElements.fromAnnotations(
            Component.literal("Skyzen Settings"),
            SkyzenModLoader.configManager.config,
        )
    }

    fun create(parent: Screen?): Screen {
        return Lattice.createConfigScreen(
            createElements(),
            {
                Minecraft.getInstance().options.save()
                SkyzenModLoader.configManager.saveConfig()
            },
            parent,
        )
    }
}
