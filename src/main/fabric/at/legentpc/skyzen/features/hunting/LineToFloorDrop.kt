package at.legentpc.skyzen.features.hunting

import at.legentpc.skyzen.SkyzenModLoader
import at.legentpc.skyzen.config.features.hunting.HuntingConfig.FloorDropIsland
import at.legentpc.skyzen.events.SkyzenEvents
import at.legentpc.skyzen.module.Module
import at.legentpc.skyzen.utils.HypixelUtils
import at.legentpc.skyzen.utils.IslandType
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Display
import net.minecraft.world.item.Items
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object LineToFloorDrop : Module("Line to Floor Drop") {

    private const val SCAN_INTERVAL_TICKS = 10
    private const val VERTICAL_SEARCH_RANGE = 8.0
    private const val COLLECT_RADIUS = 2.0
    private const val DROP_DISPLAY_COUNT = 3
    private const val DROP_TIMEOUT_MS = 5_000L
    private const val COLLECT_COOLDOWN_MS = 3_000L

    // Last time each confirmed drop was seen, and each collected drop was clicked
    private val floorDrops = mutableMapOf<BlockPos, Long>()
    private val collectedDrops = mutableMapOf<BlockPos, Long>()
    private var tickCounter = 0

    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register {
            if (++tickCounter % SCAN_INTERVAL_TICKS == 0) scanForDrops()
        }
        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register { context ->
            renderLine(context)
        }
        UseBlockCallback.EVENT.register { _, _, _, hitResult ->
            collectAt(hitResult.blockPos)
            InteractionResult.PASS
        }
        AttackBlockCallback.EVENT.register { _, _, _, pos, _ ->
            collectAt(pos)
            InteractionResult.PASS
        }
        UseEntityCallback.EVENT.register { _, _, _, entity, _ ->
            if (entity is Display.ItemDisplay) collectAt(entity.blockPosition())
            InteractionResult.PASS
        }
        AttackEntityCallback.EVENT.register { _, _, _, entity, _ ->
            if (entity is Display.ItemDisplay) collectAt(entity.blockPosition())
            InteractionResult.PASS
        }
        SkyzenEvents.WORLD_CHANGE.register {
            floorDrops.clear()
            collectedDrops.clear()
        }
    }

    private fun scanForDrops() {
        val now = System.currentTimeMillis()
        purgeExpired(now)
        if (!isEnabled()) return

        val level = Minecraft.getInstance().level ?: return
        val player = Minecraft.getInstance().player ?: return
        val hunting = SkyzenModLoader.configManager.config.hunting

        val radius = hunting.floorDropScanRadius.toDouble()
        // Extra block of Y margin: markers match on block Y, so an entity can sit up to a block above it
        val box = AABB.ofSize(player.position(), radius * 2.0, VERTICAL_SEARCH_RANGE * 2.0 + 2.0, radius * 2.0)
        val playerY = player.position().y

        level.getEntitiesOfClass(Display.ItemDisplay::class.java, box)
            .filter { it.isFloorDropMarker(playerY) }
            .groupingBy { it.blockPosition() }
            .eachCount()
            .filterValues { it == DROP_DISPLAY_COUNT }
            .forEach { (pos, _) ->
                if (pos !in collectedDrops) floorDrops[pos] = now
            }
    }

    // A floor drop is marked by three string item displays stacked in the same block
    private fun Display.ItemDisplay.isFloorDropMarker(playerY: Double): Boolean {
        val y = blockPosition().y.toDouble()
        if (y !in playerY - VERTICAL_SEARCH_RANGE..playerY + VERTICAL_SEARCH_RANGE) return false
        val stack = getItemStack()
        return !stack.isEmpty && stack.getItem() == Items.STRING
    }

    private fun collectAt(pos: BlockPos) {
        if (!isEnabled()) return
        val click = Vec3.atCenterOf(pos)
        val now = System.currentTimeMillis()
        val nearby = floorDrops.keys.filter {
            Vec3.atCenterOf(it).distanceToSqr(click) <= COLLECT_RADIUS * COLLECT_RADIUS
        }
        nearby.forEach {
            floorDrops.remove(it)
            collectedDrops[it] = now
        }
    }

    private fun purgeExpired(now: Long) {
        floorDrops.entries.removeAll { now - it.value > DROP_TIMEOUT_MS }
        collectedDrops.entries.removeAll { now - it.value > COLLECT_COOLDOWN_MS }
    }

    private fun renderLine(context: LevelRenderContext) {
        if (!isEnabled() || floorDrops.isEmpty()) return
        val player = Minecraft.getInstance().player ?: return

        val eye = player.getEyePosition()
        val nearest = floorDrops.keys.minByOrNull { Vec3.atCenterOf(it).distanceToSqr(eye) } ?: return
        val target = Vec3.atLowerCornerWithOffset(nearest, 0.5, 0.25, 0.5)

        val maxDistance = SkyzenModLoader.configManager.config.hunting.floorDropMaxDistance.toDouble()
        if (eye.distanceToSqr(target) > maxDistance * maxDistance) return

        // Level render events expect camera-relative vertices
        val camera = context.levelState().cameraRenderState.pos
        val from = eye.subtract(camera)
        val to = target.subtract(camera)

        val direction = to.subtract(from)
        if (direction.lengthSqr() < 1.0E-4) return
        val normal = direction.normalize()

        val pose = context.poseStack().last().pose()
        val buffer = context.bufferSource().getBuffer(RenderTypes.lines())
        val width = SkyzenModLoader.configManager.config.hunting.floorDropLineWidth.toFloat()

        buffer.addVertex(pose, from.x.toFloat(), from.y.toFloat(), from.z.toFloat())
            .setColor(0.2f, 1.0f, 0.4f, 0.9f)
            .setNormal(normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat())
            .setLineWidth(width)
        buffer.addVertex(pose, to.x.toFloat(), to.y.toFloat(), to.z.toFloat())
            .setColor(0.2f, 1.0f, 0.4f, 0.9f)
            .setNormal(-normal.x.toFloat(), -normal.y.toFloat(), -normal.z.toFloat())
            .setLineWidth(width)
    }

    private fun isEnabled(): Boolean {
        val hunting = SkyzenModLoader.configManager.config.hunting
        if (!hunting.lineToFloorDrop || !HypixelUtils.inSkyblock) return false
        val island = when (HypixelUtils.currentIsland) {
            IslandType.MOONGLADE_MARSH -> FloorDropIsland.MOONGLADE_MARSH
            IslandType.TORRHUS_CANYON -> FloorDropIsland.TORRHUS_CANYON
            IslandType.CRITTER_SAFARI -> FloorDropIsland.CRITTER_SAFARI
            else -> return false
        }
        return island in hunting.floorDropIslands
    }
}
