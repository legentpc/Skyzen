package at.legentpc.skyzen.events

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

object SkyzenEvents {

    @JvmField
    val ENTITY_DISPLAY_NAME: Event<EntityDisplayNameCallback> =
        EventFactory.createArrayBacked(EntityDisplayNameCallback::class.java) { listeners ->
            EntityDisplayNameCallback { event ->
                for (listener in listeners) {
                    listener.onDisplayName(event)
                }
            }
        }

    @JvmField
    val ISLAND_CHANGE: Event<IslandChangeEvent> =
        EventFactory.createArrayBacked(IslandChangeEvent::class.java) { listeners ->
            IslandChangeEvent { newIsland, oldIsland ->
                for (listener in listeners) {
                    listener.onIslandChange(newIsland, oldIsland)
                }
            }
        }

    @JvmField
    val WORLD_CHANGE: Event<WorldChangeEvent> =
        EventFactory.createArrayBacked(WorldChangeEvent::class.java) { listeners ->
            WorldChangeEvent {
                for (listener in listeners) {
                    listener.onWorldChange()
                }
            }
        }
}
