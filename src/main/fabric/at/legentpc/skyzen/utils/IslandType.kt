package at.legentpc.skyzen.utils

enum class IslandType(val displayName: String, vararg val areas: String) {

    HUB("Hub", "Hub"),
    GARDEN("Garden", "Garden"),
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
