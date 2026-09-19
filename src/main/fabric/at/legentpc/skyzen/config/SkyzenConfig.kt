package at.legentpc.skyzen.config

import at.legentpc.skyzen.config.features.hunting.HuntingConfig
import at.legentpc.skyzen.config.features.misc.MiscConfig
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.Config
import io.github.notenoughupdates.moulconfig.annotations.Category
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class SkyzenConfig : Config() {

    @Expose
    @Category(name = "General", desc = "General Settings")
    var general: GeneralConfig = GeneralConfig()

    @Expose
    @Category(name = "Hunting", desc = "Hunting Features")
    var hunting: HuntingConfig = HuntingConfig()

    @Expose
    @Category(name = "Misc", desc = "Miscellaneous Features")
    var misc: MiscConfig = MiscConfig()

    class GeneralConfig {

        @Expose
        @ConfigOption(name = "Enable Skyzen", desc = "Enable all Skyzen mod features")
        @ConfigEditorBoolean
        var enabled: Boolean = true
    }
}
