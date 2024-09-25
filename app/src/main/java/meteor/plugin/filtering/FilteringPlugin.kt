package meteor.plugin.filtering

import androidx.compose.ui.graphics.FilterQuality
import meteor.Main.client
import meteor.events.client.ConfigChanged
import meteor.plugin.Plugin
import meteor.plugin.filtering.FilteringConfig
import meteor.plugin.filtering.Quality
import meteor.ui.compose.components.GamePanel
import meteor.ui.config.AspectMode

class FilteringPlugin : Plugin("Filtering") {
    val config = configuration<FilteringConfig>()
    override fun onStart() {
        updateFilter()
    }

    override fun onStop() {
        GamePanel.filter.value = FilterQuality.None
    }

    override fun onConfigChanged(it: ConfigChanged) {
        updateFilter()
    }

    fun updateFilter() {
        GamePanel.filter.value = when (config.quality.get<Quality>()) {
            Quality.NEAREST_NEIGHBOR -> FilterQuality.None
            Quality.BILINEAR -> FilterQuality.Low
            Quality.BILINEAR_MIPMAP -> FilterQuality.Medium
            Quality.BICUBIC -> FilterQuality.High
        }
    }
}