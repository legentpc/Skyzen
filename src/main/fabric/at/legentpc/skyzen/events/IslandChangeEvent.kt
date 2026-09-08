package at.legentpc.skyzen.events

import at.legentpc.skyzen.utils.IslandType

fun interface IslandChangeEvent {
    fun onIslandChange(newIsland: IslandType, oldIsland: IslandType)
}
