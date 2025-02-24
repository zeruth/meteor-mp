package meteor.platform.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import meteor.platform.android.ui.batteryfps.BatteryFpsDisplayButton
import meteor.platform.android.Game.image
import meteor.platform.common.ui.components.panel.PanelComposables.Panel
import meteor.platform.common.ui.UI.configWidth
import meteor.platform.common.ui.UI.panelOpen
import meteor.platform.common.ui.UI.sidebarWidth
import meteor.platform.common.ui.UI.uiSide
import meteor.platform.android.ui.GamePanel.Game
import meteor.platform.common.ui.components.sidebar.SidebarComposables.Sidebar
import meteor.platform.common.ui.components.sidebar.UISide
import meteor.platform.android.input.KeyboardButton
import meteor.platform.android.ui.map.MapView
import meteor.platform.android.ui.map.MapViewButton
import meteor.platform.android.ui.worlds.WorldsButton

/**
 * The main entry point to the Compose UI
 */
object Window {

    val sidebarButtons = arrayOf(WorldsButton(), MapViewButton(), KeyboardButton(), BatteryFpsDisplayButton())

    @Composable
    fun BoxScope.MeteorViewBox() {
        if (image.value == null)
            return
        Row(Modifier.fillMaxSize()) {
            when (uiSide.value) {
                UISide.RIGHT -> {
                    Box(Modifier.fillMaxHeight().weight(1f).background(Color.Black)) {
                        if (MapView.mapVisible.value)
                            MapView.MapView()
                        else
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
                        if (MapView.mapVisible.value)
                            MapView.MapView()
                        else
                            Game()
                    }
                }
            }
        }
    }
}