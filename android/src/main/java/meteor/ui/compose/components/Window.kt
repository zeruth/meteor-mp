package meteor.ui.compose.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.meteor.android.MainActivity
import com.meteor.android.MainActivity.Companion.image
import meteor.ui.compose.components.GamePanel.Game
import meteor.ui.compose.components.panel.PanelComposables.Panel
import meteor.ui.compose.components.sidebar.SidebarComposables.Sidebar
import meteor.ui.compose.components.sidebar.UISide
import java.awt.Dimension

/**
 * The main entry point to the Compose UI
 */
object Window {
    val sidebarWidth = mutableStateOf(40.dp)
    val configWidth = mutableStateOf(300.dp)
    var panelOpen = mutableStateOf(false)
    val uiSide = mutableStateOf(UISide.RIGHT)
    var gameWidth = mutableStateOf((-1).dp)

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