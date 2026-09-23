package at.legentpc.skyzen

import at.legentpc.skyzen.config.SkyzenConfigManager
import at.legentpc.skyzen.features.hunting.LineToFloorDrop
import at.legentpc.skyzen.features.misc.GiftCleanDisplay
import at.legentpc.skyzen.utils.HypixelUtils
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.minecraft.network.chat.Component

object SkyzenModLoader : ClientModInitializer {

    val configManager: SkyzenConfigManager = SkyzenConfigManager()

    override fun onInitializeClient() {
        HypixelUtils.init()
        registerFeatures()
        registerCommands()
        registerShutdownHook()
    }

    private fun registerFeatures() {
        GiftCleanDisplay.init()
        LineToFloorDrop.init()
    }

    private fun registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                LiteralArgumentBuilder.literal<FabricClientCommandSource>("skyzen")
                    .executes {
                        it.source.sendFeedback(
                            Component.literal("Skyzen config: ${configManager.configFile.absolutePath}")
                        )
                        1
                    }
            )
        }
    }

    private fun registerShutdownHook() {
        ClientLifecycleEvents.CLIENT_STOPPING.register {
            configManager.saveConfig()
        }
    }
}
