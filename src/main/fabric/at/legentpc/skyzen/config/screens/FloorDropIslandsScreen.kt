package at.legentpc.skyzen.config.screens

import at.legentpc.skyzen.SkyzenModLoader
import at.legentpc.skyzen.config.features.hunting.HuntingConfig.FloorDropIsland
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Checkbox
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.FrameLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.layouts.SpacerElement
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class FloorDropIslandsScreen(
    private val parent: Screen?,
) : Screen(Component.literal("Floor Drop Islands")) {

    override fun init() {
        val enabledIslands = SkyzenModLoader.configManager.config.hunting.floorDropIslands
        val layout = LinearLayout.vertical().spacing(4)

        layout.addChild(StringWidget(title, font))
        layout.addChild(SpacerElement.height(8))

        val islandList = layout.addChild(LinearLayout.vertical()).spacing(4)

        for (island in FloorDropIsland.entries) {
            val checkbox = Checkbox.builder(
                Component.literal(island.displayName),
                font,
            )
                .selected(island in enabledIslands)
                .onValueChange { _, enabled ->
                    if (enabled) {
                        if (island !in enabledIslands) {
                            enabledIslands.add(island)
                        }
                    } else {
                        enabledIslands.remove(island)
                    }
                }
                .build()

            islandList.addChild(checkbox)
        }

        islandList.arrangeElements()
        layout.addChild(SpacerElement.height(8))

        val doneButton = Button.builder(CommonComponents.GUI_DONE) {
            onClose()
        }
            .width(islandList.width)
            .build()

        layout.addChild(doneButton)
        layout.visitWidgets(::addRenderableWidget)
        layout.arrangeElements()
        FrameLayout.centerInRectangle(layout, rectangle)
    }

    override fun onClose() {
        minecraft.setScreen(parent)
    }
}
