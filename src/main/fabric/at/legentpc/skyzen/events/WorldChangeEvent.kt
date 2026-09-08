package at.legentpc.skyzen.events

/**
 * Fired after the client switched to a different world, meaning the [net.minecraft.client.multiplayer.ClientLevel]
 * instance was replaced (server switch, world join), or the SkyBlock sidebar disappeared (disconnect).
 *
 * Fired on the main client thread, primarily via the Fabric `ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE`
 * callback. On Hypixel every server switch replaces the world, so this fires on every island change as well.
 *
 * This is the standard event for resetting world bound state, for example cached entities, positions or timers.
 *
 * Do not use this when the concrete island matters. This event carries no [at.legentpc.skyzen.utils.IslandType]
 * and also fires for world changes that are not island changes. Use [IslandChangeEvent] instead, which reports
 * both the old and the new island.
 *
 * @see IslandChangeEvent
 */
fun interface WorldChangeEvent {
    fun onWorldChange()
}
