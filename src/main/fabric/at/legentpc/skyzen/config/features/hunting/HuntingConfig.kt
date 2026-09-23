package at.legentpc.skyzen.config.features.hunting

class HuntingConfig {

    var lineToFloorDrop: Boolean = true

    var floorDropLineWidth: Int = 3 // 1-10

    val floorDropIslands: MutableList<FloorDropIsland> = mutableListOf(
        FloorDropIsland.MOONGLADE_MARSH,
        FloorDropIsland.TORRHUS_CANYON,
        FloorDropIsland.CRITTER_SAFARI,
    )

    enum class FloorDropIsland(val displayName: String) {
        MOONGLADE_MARSH("Moonglade Marsh"),
        TORRHUS_CANYON("Torrhus Canyon"),
        CRITTER_SAFARI("Critter Safari"),
        ;

        override fun toString() = displayName
    }

    var floorDropMaxDistance: Int = 15 // 5-128

    var floorDropScanRadius: Int = 20 // 20-64
}
