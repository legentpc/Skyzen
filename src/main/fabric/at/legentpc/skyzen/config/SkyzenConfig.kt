package at.legentpc.skyzen.config

import at.legentpc.skyzen.config.features.hunting.HuntingConfig
import at.legentpc.skyzen.config.features.misc.MiscConfig

class SkyzenConfig {

    val hunting: HuntingConfig = HuntingConfig()

    val misc: MiscConfig = MiscConfig()
}
