package meteor.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import meteor.MainActivity.Companion.image
import meteor.common.Common.configWidth
import meteor.common.Common.panelOpen
import meteor.common.Common.sidebarWidth
import meteor.common.Common.uiSide
import meteor.common.panel.PanelComposables.Panel
import meteor.ui.GamePanel.Game
import meteor.common.ui.components.sidebar.SidebarComposables.Sidebar
import meteor.common.ui.components.sidebar.UISide

/**
 * The main entry point to the Compose UI
 */
object Window {


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun BoxScope.ViewBox() {
        if (image.value == null)
            return
        Row(Modifier.fillMaxSize().pointerInteropFilter {
            Log.d("", "pos: $it.x ${it.y}")
            false
        }) {
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
                        Sidebar()
                    }
                }

                UISide.LEFT -> {
                    Box(Modifier.fillMaxHeight().width(sidebarWidth.value)) {
                        Sidebar()
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