package at.legentpc.skyzen

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Skyzen : ModInitializer {

    const val MOD_ID = "skyzen"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        logger.info("Skyzen initialized")
    }
}
