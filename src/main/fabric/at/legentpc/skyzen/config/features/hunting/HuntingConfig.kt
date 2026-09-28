package at.legentpc.skyzen.config.features.hunting

import at.legentpc.skyzen.config.screens.FloorDropIslandsScreen
import com.moulberry.lattice.WidgetFunction
import com.moulberry.lattice.annotation.LatticeOption
import com.moulberry.lattice.annotation.constraint.LatticeIntRange
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton
import com.moulberry.lattice.annotation.widget.LatticeWidgetCustom
import com.moulberry.lattice.annotation.widget.LatticeWidgetSlider
import net.minecraft.client.Minecraft
import java.util.function.Consumer
import java.util.function.Supplier

class HuntingConfig {

    @LatticeOption(
        title = "Line to Floor Drop",
        description = "Draws a line to nearby hunting floor drops.",
    )
    @LatticeWidgetButton
    var lineToFloorDrop: Boolean = true

    @LatticeOption(
        title = "Line Width",
        description = "Controls the width of floor drop lines.",
    )
    @LatticeIntRange(min = 1, max = 10, clampMin = 1, clampMax = 10)
    @LatticeWidgetSlider
    var floorDropLineWidth: Int = 3

    @LatticeOption(
        title = "Floor Drop Islands",
        description = "Select the islands where floor drop lines are enabled.",
    )
    @LatticeWidgetCustom(function = "openFloorDropIslandsScreen")
    @Transient
    var floorDropIslandsButton: Int = 0

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

    @LatticeOption(
        title = "Maximum Distance",
        description = "Maximum distance at which floor drop lines are rendered.",
    )
    @LatticeIntRange(min = 5, max = 128, clampMin = 5, clampMax = 128)
    @LatticeWidgetSlider
    var floorDropMaxDistance: Int = 15

    @LatticeOption(
        title = "Scan Radius",
        description = "Radius used to scan for nearby floor drops.",
    )
    @LatticeIntRange(min = 20, max = 64, clampMin = 20, clampMax = 64)
    @LatticeWidgetSlider
    var floorDropScanRadius: Int = 20

    companion object {

        @Suppress("unused")
        @JvmStatic
        fun openFloorDropIslandsScreen(
            supplier: Supplier<Int>,
            consumer: Consumer<Int>,
        ): WidgetFunction {
            return WidgetFunction.runnableButton {
                val client = Minecraft.getInstance()
                client.setScreen(FloorDropIslandsScreen(client.screen))
            }
        }
    }
}
