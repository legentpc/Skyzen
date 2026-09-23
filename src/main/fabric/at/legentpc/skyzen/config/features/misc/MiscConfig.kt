package at.legentpc.skyzen.config.features.misc

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class MiscConfig {

    @Expose
    @ConfigOption(name = "Clean Gift Nametags", desc = "Cleans the From: / To: nametags above gift entities in Skyblock")
    @ConfigEditorBoolean
    var cleanGiftNametags: Boolean = true
}
