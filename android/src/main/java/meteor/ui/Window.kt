package meteor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import meteor.platform.BatteryFpsDisplayButton
import meteor.Game.image
import meteor.common.ui.components.panel.PanelComposables.Panel
import meteor.common.ui.UI.configWidth
import meteor.common.ui.UI.panelOpen
import meteor.common.ui.UI.sidebarWidth
import meteor.common.ui.UI.uiSide
import meteor.ui.GamePanel.Game
import meteor.common.ui.components.sidebar.SidebarComposables.Sidebar
import meteor.common.ui.components.sidebar.UISide
import meteor.input.KeyboardButton

/**
 * The main entry point to the Compose UI
 */
object Window {

    val sidebarButtons = arrayOf(WorldsButton(), KeyboardButton(), BatteryFpsDisplayButton())

    @Composable
    fun BoxScope.MeteorViewBox() {
        if (image.value == null)
            return
        Row(Modifier.fillMaxSize()) {
            when (uiSide.value) {
                UISide.RIGHT -> {
                    Box(Modifier.fillMaxHeight().weight(1f).background(Color.Black)) {
                        Game()
                    }
                    if (panelOpen.value) {
                        Box(Modifier.fillMaxHeight().width(configWidth.value)) {
                            Panel()
                        }
                    }
                    Box(Modifier.fillMaxHeight().width(sidebarWidth.value)) {
                        Sidebar(*sidebarButtons)
                    }
                }

                UISide.LEFT -> {
                    Box(Modifier.fillMaxHeight().width(sidebarWidth.value)) {
                        Sidebar(*sidebarButtons)
                    }
                    if (panelOpen.value) {
                        Box(Modifier.fillMaxHeight().width(configWidth.value)) {
                            Panel()
                        }
                    }
                    Box(Modifier.fillMaxHeight().weight(1f)) {
                        Game()
                    }
                }
            }
        }
    }
}