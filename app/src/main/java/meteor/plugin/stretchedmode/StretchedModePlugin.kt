package meteor.plugin.stretchedmode

import meteor.Main.client
import meteor.plugin.Plugin
import meteor.ui.compose.components.GamePanel
import meteor.ui.config.AspectMode

class StretchedModePlugin : Plugin("Stretched Mode") {
    override fun onStart() {
        client.aspectMode = AspectMode.FILL
        GamePanel.aspectMode.value = client.aspectMode
    }

    override fun onStop() {
        client.aspectMode = AspectMode.FIT
        GamePanel.aspectMode.value = client.aspectMode
    }
}