package at.legentpc.skyzen.config.features.misc

import com.moulberry.lattice.annotation.LatticeOption
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton

class MiscConfig {

    @LatticeOption(
        title = "Clean Gift Nametags",
        description = "Removes unnecessary text from gift nametags.",
    )
    @LatticeWidgetButton
    var cleanGiftNametags: Boolean = true
}
