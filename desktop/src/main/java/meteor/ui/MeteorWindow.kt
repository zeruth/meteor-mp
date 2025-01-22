package meteor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import meteor.common.Common.panelOpen
import meteor.Game.gameImage
import meteor.Game.loadingImage
import meteor.common.panel.PanelComposables.Panel
import meteor.common.ui.components.sidebar.SidebarComposables
import meteor.ui.GameView.GameViewContainer
import meteor.ui.GameView.stretchedMode
import meteor.ui.buttons.CloseMeteorButton
import meteor.ui.buttons.DiscordStatusButton
import meteor.ui.buttons.FullscreenToggleButton
import meteor.ui.buttons.StretchToggleButton
import java.awt.Dimension

object MeteorWindow {
    val sidebarWidth = mutableStateOf(40.dp)
    val configWidth = mutableStateOf(300.dp)
    var fixedWindowSize = Dimension(789 + sidebarWidth.value.value.toInt(), 532)
    var fixedState = mutableStateOf(true)
    val floatingState = WindowState(
        position = WindowPosition(Alignment.Center),
        placement = WindowPlacement.Floating)
    val fullscreenState = WindowState(
        position = WindowPosition(Alignment.Center),
        placement = WindowPlacement.Fullscreen)
    val windowState = mutableStateOf(floatingState)
    lateinit var windowInstance: ComposeWindow

    var panelWasOpen = false

    fun resetWindowSize() {
        windowInstance.minimumSize = Dimension(fixedWindowSize.width, fixedWindowSize.height)
        val resetBounds = !stretchedMode.value
        val height = if (resetBounds) fixedWindowSize.height else windowInstance.height
        var width = if (resetBounds) fixedWindowSize.width else windowInstance.width

        if (panelWasOpen != panelOpen.value) {
            if (panelOpen.value) {
                width += configWidth.value.value.toInt()
            } else {
                width -= configWidth.value.value.toInt()
            }
            panelWasOpen = panelOpen.value
        }

        if (windowState.value == fullscreenState) {
            windowState.value = fullscreenState
        } else {
            windowInstance.size = Dimension(width, height)
        }

    }

    val closeMeteorButton = CloseMeteorButton()
    val discordStatusButton = DiscordStatusButton()
    val fullscreenToggleButton = FullscreenToggleButton()
    val stretchToggleButton = StretchToggleButton()


    @Composable
    fun ApplicationScope.MeteorWindow() {
        key(windowState.value) {
            Window(
                onCloseRequest = ::exitApplication,
                title = "Meteor 225 (2.1.0)",
                state = windowState.value,
                undecorated = windowState.value == fullscreenState,
                resizable = !fixedState.value || (stretchedMode.value && windowState.value != fullscreenState)
            ) {
                windowInstance = this.window
                val finalImage = if (gameImage.value != null) gameImage else loadingImage
                Row {
                    GameViewContainer(finalImage.value!!)
                    if (panelOpen.value) {
                        Box(Modifier.fillMaxHeight().width(configWidth.value)) {
                            Panel()
                        }
                    }
                    val currentButtons = mutableSetOf(discordStatusButton, stretchToggleButton, fullscreenToggleButton)
                    if (windowState.value != fullscreenState) {
                        SidebarComposables.remove(closeMeteorButton)
                    } else {
                        currentButtons.add(closeMeteorButton)
                    }

                    SidebarComposables.Sidebar(*currentButtons.toTypedArray())
                }
            }

            LaunchedEffect(Unit) {
                resetWindowSize()
            }
        }
    }
}