package at.legentpc.skyzen.utils

import at.legentpc.skyzen.events.SkyzenEvents
import at.legentpc.skyzen.module.Module
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft
import net.minecraft.world.scores.DisplaySlot

object HypixelUtils : Module("HypixelUtils", needsToggle = false) {

    var currentIsland: IslandType = IslandType.UNKNOWN
        private set
    var inSkyblock: Boolean = false
        private set

    private val colorCode = Regex("§.")
    private var tickCounter = 0

    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register {
            if (++tickCounter % 10 == 0) onTick()
        }
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register { _, _ ->
            onWorldChange()
        }
    }

    private fun onTick() {
        val scoreboard = Minecraft.getInstance().level?.scoreboard
        val objective = scoreboard?.getDisplayObjective(DisplaySlot.SIDEBAR)

        if (scoreboard == null || objective == null) {
            if (inSkyblock) onWorldChange()
            return
        }

        val wasInSkyblock = inSkyblock
        inSkyblock = objective.displayName.string.contains("SKYBLOCK", ignoreCase = true)

        if (!inSkyblock) {
            if (wasInSkyblock) onWorldChange()
            return
        }

        val lines = scoreboard.listPlayerScores(objective)
            .map { it.owner() }

        val areaLine = lines.firstOrNull { it.contains("⏣") || it.contains("ф") }
        if (areaLine != null) {
            val cleaned = areaLine.replace(colorCode, "").replace("⏣", "").replace("ф", "").trim()
            updateIsland(IslandType.fromArea(cleaned))
        }
    }

    private fun onWorldChange() {
        inSkyblock = false
        updateIsland(IslandType.UNKNOWN)
        SkyzenEvents.WORLD_CHANGE.invoker().onWorldChange()
    }

    private fun updateIsland(newIsland: IslandType) {
        if (newIsland == currentIsland) return
        val oldIsland = currentIsland
        currentIsland = newIsland
        SkyzenEvents.ISLAND_CHANGE.invoker().onIslandChange(newIsland, oldIsland)
    }
}
