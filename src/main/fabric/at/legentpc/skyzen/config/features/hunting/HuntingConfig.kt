package at.legentpc.skyzen.config.features.hunting

import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDraggableList
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class HuntingConfig {

    @ConfigOption(
        name = "Line to Floor Drop",
        desc = "Draws a line to the nearest Floor Drop on the ground.",
    )
    @ConfigEditorBoolean
    var lineToFloorDrop: Boolean = true

    @ConfigOption(
        name = "Line to Floor Drop Width",
        desc = "The width of the line pointing to the nearest Floor Drop.",
    )
    @ConfigEditorSlider(minStep = 1f, minValue = 1f, maxValue = 10f)
    var floorDropLineWidth: Int = 3

    @ConfigOption(
        name = "Floor Drop Islands",
        desc = "Select the islands where the Line to Floor Drop feature should work.",
    )
    @ConfigEditorDraggableList
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

    @ConfigOption(
        name = "Line to Floor Drop Distance",
        desc = "Maximum distance between you and a Floor Drop for the line to be shown.",
    )
    @ConfigEditorSlider(minStep = 1f, minValue = 5f, maxValue = 128f)
    var floorDropMaxDistance: Int = 15

    @ConfigOption(
        name = "Floor Drop Scan Radius",
        desc = "Radius in blocks to scan for Floor Drops around the player. Higher values may impact performance.",
    )
    @ConfigEditorSlider(minStep = 1f, minValue = 20f, maxValue = 64f)
    var floorDropScanRadius: Int = 20
}
