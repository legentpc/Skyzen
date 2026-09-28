package at.legentpc.skyzen.compat

import at.legentpc.skyzen.config.SkyzenConfigScreen
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.minecraft.client.gui.screens.Screen

class ModMenuCompatibility : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<Screen> {
        return { parent: Screen? ->
            SkyzenConfigScreen.create(parent)
        }
    }
}
