package at.legentpc.skyzen.config.features.misc

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class MiscConfig {

    @Expose
    @ConfigOption(name = "Hide Gift Nametag", desc = "Hides the nametag above gifts in Skyblock")
    @ConfigEditorBoolean
    var hideGiftNametag: Boolean = true
}
