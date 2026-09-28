package at.legentpc.skyzen.features.misc

import at.legentpc.skyzen.SkyzenModLoader
import at.legentpc.skyzen.events.EntityDisplayNameCallback
import at.legentpc.skyzen.events.EntityDisplayNameEvent
import at.legentpc.skyzen.events.SkyzenEvents
import at.legentpc.skyzen.module.Module
import net.minecraft.network.chat.Component

object GiftCleanDisplay : Module("Gift Clean Display") {

    private val colorCode = Regex("§[0-9a-fk-orA-FK-OR]")

    @JvmStatic
    fun isEnabled(): Boolean {
        return SkyzenModLoader.configManager.config.misc.cleanGiftNametags
    }

    fun init() {
        SkyzenEvents.ENTITY_DISPLAY_NAME.register(object : EntityDisplayNameCallback {
            override fun onDisplayName(event: EntityDisplayNameEvent) {
                if (!isEnabled()) return
                val clean = event.displayName.string.replace(colorCode, "").trim()
                if (clean.startsWith("From:") || clean.startsWith("To:")) {
                    event.displayName = Component.literal("")
                }
            }
        })
    }
}
