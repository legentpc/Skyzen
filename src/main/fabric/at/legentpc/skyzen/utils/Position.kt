package at.legentpc.skyzen.utils

import com.google.gson.annotations.Expose

@Suppress("unused") // Reserved for future position-based configuration.
data class Position(
    @Expose var x: Int = 100,
    @Expose var y: Int = 100,
)
