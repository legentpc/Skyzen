package at.legentpc.skyzen.utils

enum class IslandType(val displayName: String, vararg val areas: String) {

    HUB("Hub", "Hub"),
    GARDEN("Garden", "Garden"),
    MOONGLADE_MARSH("Moonglade Marsh", "Moonglade Marsh"),
    TORRHUS_CANYON("Torrhus Canyon", "Torrhus Canyon"),
    CRITTER_SAFARI("Critter Safari", "Safari"),
    UNKNOWN("Unknown");

    fun isActive(): Boolean = this == HypixelUtils.currentIsland

    companion object {
        fun fromArea(area: String): IslandType {
            return entries.firstOrNull { type ->
                type.areas.any { it.equals(area, ignoreCase = true) }
            } ?: UNKNOWN
        }
    }
}
