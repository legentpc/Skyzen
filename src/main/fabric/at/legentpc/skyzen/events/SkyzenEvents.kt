package at.legentpc.skyzen.events

import at.legentpc.skyzen.utils.IslandType
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

object SkyzenEvents {

    @JvmField
    val ENTITY_DISPLAY_NAME: Event<EntityDisplayNameCallback> =
        EventFactory.createArrayBacked(EntityDisplayNameCallback::class.java, ::entityDisplayNameInvoker)

    @JvmField
    val ISLAND_CHANGE: Event<IslandChangeEvent> =
        EventFactory.createArrayBacked(IslandChangeEvent::class.java, ::islandChangeInvoker)

    @JvmField
    val WORLD_CHANGE: Event<WorldChangeEvent> =
        EventFactory.createArrayBacked(WorldChangeEvent::class.java, ::worldChangeInvoker)

    private fun entityDisplayNameInvoker(listeners: Array<EntityDisplayNameCallback>): EntityDisplayNameCallback =
        EntityDisplayNameInvoker(listeners)

    private fun islandChangeInvoker(listeners: Array<IslandChangeEvent>): IslandChangeEvent =
        IslandChangeInvoker(listeners)

    private fun worldChangeInvoker(listeners: Array<WorldChangeEvent>): WorldChangeEvent =
        WorldChangeInvoker(listeners)
}

// Plain invoker classes instead of lambdas: no $lambda$ / $listeners
// synthetic names end up in the jar this way.
private class EntityDisplayNameInvoker(private val listeners: Array<EntityDisplayNameCallback>) : EntityDisplayNameCallback {
    override fun onDisplayName(event: EntityDisplayNameEvent) {
        for (listener in listeners) {
            listener.onDisplayName(event)
        }
    }
}

private class IslandChangeInvoker(private val listeners: Array<IslandChangeEvent>) : IslandChangeEvent {
    override fun onIslandChange(newIsland: IslandType, oldIsland: IslandType) {
        for (listener in listeners) {
            listener.onIslandChange(newIsland, oldIsland)
        }
    }
}

private class WorldChangeInvoker(private val listeners: Array<WorldChangeEvent>) : WorldChangeEvent {
    override fun onWorldChange() {
        for (listener in listeners) {
            listener.onWorldChange()
        }
    }
}
