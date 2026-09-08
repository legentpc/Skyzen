package at.legentpc.skyzen.events

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity

class EntityDisplayNameEvent(
    val entity: Entity,
    var displayName: Component
)

fun interface EntityDisplayNameCallback {
    fun onDisplayName(event: EntityDisplayNameEvent)
}
