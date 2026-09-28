package at.legentpc.skyzen.config

import at.legentpc.skyzen.config.features.hunting.HuntingConfig
import at.legentpc.skyzen.config.features.misc.MiscConfig
import com.moulberry.lattice.annotation.LatticeCategory

class SkyzenConfig {

    @LatticeCategory(name = "Hunting")
    val hunting: HuntingConfig = HuntingConfig()

    @LatticeCategory(name = "Miscellaneous")
    val misc: MiscConfig = MiscConfig()
}
