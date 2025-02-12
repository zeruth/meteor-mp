package meteor.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import client.events.DrawFinished
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CompressArrowsAltSolid
import compose.icons.lineawesomeicons.ExpandArrowsAltSolid
import compose.icons.lineawesomeicons.LockSolid
import compose.icons.lineawesomeicons.UnlockSolid
import meteor.common.Common.clientInstance
import meteor.common.Common.eventbus
import meteor.common.config.ConfigManager
import meteor.common.ui.UI.filterQuality
import meteor.common.ui.components.sidebar.SidebarComposables
import meteor.ui.MeteorWindow.fixedState
import meteor.ui.MeteorWindow.fixedWindowSize
import meteor.ui.MeteorWindow.resetWindowSize
import meteor.ui.MeteorWindow.windowState
import meteor.ui.buttons.FullscreenToggleButton
import meteor.ui.buttons.StretchToggleButton
import java.awt.Dimension

object GameView {

    var scaleX = -1f
    var scaleY = -1f

    val focusRequester = FocusRequester()
    val stretchedMode = mutableStateOf(ConfigManager.get<Boolean>("meteor.stretched", false))
    var fps = mutableIntStateOf(0)
    var recentDraws = ArrayList<Long>()

    init {
        eventbus.subscribe<DrawFinished> {
            val expiredTimes = ArrayList<Long>()
            recentDraws.add(System.currentTimeMillis())
            for (renderTime in recentDraws) {
                if (renderTime < (System.currentTimeMillis() - 1000))
                    expiredTimes += renderTime
            }
            for (expiredTime in expiredTimes)
                recentDraws.remove(expiredTime)
            fps.intValue = recentDraws.size
        }
    }

    @Composable
    fun RowScope.GameViewContainer(src: ImageBitmap) {
        var mod = Modifier
            .defaultMinSize(789.dp, 532.dp)
            .weight(1f)
            .onSizeChanged { newSize ->
                if (fixedState.value) {
                    if (newSize.width < 788) {
                        val adjustWidth = (789 - newSize.width)
                        fixedWindowSize = Dimension((fixedWindowSize.width + adjustWidth), fixedWindowSize.height)
                    }
                    if (newSize.height < 531) {
                        val adjustHeight = (532 - newSize.height)
                        fixedWindowSize = Dimension((fixedWindowSize.width), fixedWindowSize.height + adjustHeight)
                    }
                    if (newSize.width > 790) {
                        val adjustWidth = (newSize.width - 789)
                        fixedWindowSize = Dimension((fixedWindowSize.width - adjustWidth), fixedWindowSize.height)
                    }
                    if (newSize.height >  533) {
                        val adjustHeight = (newSize.height - 532)
                        fixedWindowSize = Dimension((fixedWindowSize.width), fixedWindowSize.height - adjustHeight)
                    }
                    fixedWindowSize = Dimension(fixedWindowSize.width.coerceAtLeast(789), fixedWindowSize.height.coerceAtLeast(532))
                    resetWindowSize()
                }
            scaleX = if (stretchedMode.value) (789f / newSize.width) else 1f
            scaleY = if (stretchedMode.value) (532f / newSize.height) else 1f
        }

        if (stretchedMode.value) {
            mod = mod.fillMaxSize()
        }

        Box(mod) {
            GameViewImage(src)
            Text("FPS: ${fps.intValue}", Modifier.align(Alignment.TopEnd), color = Color.Yellow)
        }
    }

    @Composable
    fun GameViewImage(src: ImageBitmap) {
        key(windowState.value) {
            var mod = Modifier
                .focusable()
                .registerMouseMoveListener()
                .registerDragListener()
                .registerLeftClickListener()
                .registerRightClickListener()
                .registerKeyListener()
            if (stretchedMode.value) {
                mod = mod.fillMaxSize()
            }
            Image(
                src,
                contentDescription = "GameView",
                contentScale = if (stretchedMode.value) ContentScale.FillBounds else ContentScale.None,
                filterQuality = filterQuality.value.composeValue,
                modifier = mod
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }

    fun Modifier.registerDragListener(): Modifier {
        return this.pointerInput(Unit) {
            detectDragGestures(onDragStart = {
                sendLeftClick(it.x.toInt(), it.y.toInt(), false)
            }, onDragCancel = {
                clientInstance.mouseReleased(false)
            }, onDragEnd = {
                clientInstance.mouseReleased(false)
            }) { change, dragAmount ->
                sendMouseMove(change.position.x.toInt(), change.position.y.toInt())
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun Modifier.registerRightClickListener(): Modifier {
        return this.pointerInput(Unit) {
            detectTapGestures(matcher = PointerMatcher.mouse(PointerButton.Secondary), onTap = { offset ->
                val adjustedX = (offset.x * scaleX)
                val adjustedY = (offset.y * scaleY)
                clientInstance.mouseMoved(adjustedX.toInt(), adjustedY.toInt())
                clientInstance.mousePressed(3, false, adjustedX.toInt(), adjustedY.toInt())
                clientInstance.mouseReleased(false)
            })
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun Modifier.registerLeftClickListener(): Modifier {
        return this.pointerInput(Unit) {
            detectTapGestures(matcher = PointerMatcher.mouse(PointerButton.Primary), onTap = { offset ->
                focusRequester.requestFocus()
                sendLeftClick(offset.x.toInt(), offset.y.toInt())
            })
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    fun Modifier.registerMouseMoveListener(): Modifier {
        return this.onPointerEvent(PointerEventType.Move) {
            val pos = it.changes.first().position
            sendMouseMove(pos.x.toInt(), pos.y.toInt())
        }
    }

    fun toggleFullscreen() {
        if (windowState.value == MeteorWindow.floatingState) {
            MeteorWindow.fullscreenState = WindowState(
                position = WindowPosition(Alignment.Center),
                placement = WindowPlacement.Fullscreen)
            windowState.value = MeteorWindow.fullscreenState
            stretchedMode.value = true
            SidebarComposables.getButton<FullscreenToggleButton>().icon.value = LineAwesomeIcons.CompressArrowsAltSolid
            ConfigManager.set("meteor.fullscreen", true)
        } else {
            windowState.value = MeteorWindow.floatingState
            stretchedMode.value = false
            fixedState.value = true
            SidebarComposables.getButton<FullscreenToggleButton>().icon.value = LineAwesomeIcons.ExpandArrowsAltSolid
            ConfigManager.set("meteor.fullscreen", false)
            resetWindowSize()
        }
    }

    fun Modifier.registerKeyListener(): Modifier {
        return this.onKeyEvent { keyEvent: KeyEvent ->
            keyEvent.awtEventOrNull?.let {
                if (keyEvent.type == KeyEventType.KeyDown) {
                    if (keyEvent.key == Key.F11) {
                        toggleFullscreen()
                        return@onKeyEvent true
                    }
                    if (keyEvent.key == Key.F12) {
                        if (windowState.value == MeteorWindow.floatingState) {
                            if (!stretchedMode.value) {
                                fixedState.value = false
                                stretchedMode.value = true
                                windowState.value = MeteorWindow.floatingState
                                SidebarComposables.getButton<StretchToggleButton>().icon.value = LineAwesomeIcons.LockSolid
                            } else {
                                stretchedMode.value = false
                                fixedState.value = true
                                SidebarComposables.getButton<StretchToggleButton>().icon.value = LineAwesomeIcons.UnlockSolid
                                resetWindowSize()
                            }
                        }
                        return@onKeyEvent true
                    }
                    clientInstance.keyPressed(it)
                }
                if (keyEvent.type == KeyEventType.KeyUp) {
                    clientInstance.keyReleased(it)
                }
            }
            true
        }
    }

    fun sendMouseMove(x: Int, y: Int) {
        val offset = Offset(x.toFloat(), y.toFloat())
        val adjustedX = (offset.x * scaleX)
        val adjustedY = (offset.y * scaleY)
        clientInstance.mouseMoved(adjustedX.toInt(), adjustedY.toInt())
    }

    fun sendLeftClick(x: Int, y: Int, release: Boolean = true) {
        val adjustedX = (x * scaleX)
        val adjustedY = (y * scaleY)
        clientInstance.mouseMoved(adjustedX.toInt(), adjustedY.toInt())
        clientInstance.mousePressed(1, false, adjustedX.toInt(), adjustedY.toInt())
        if (release)
            clientInstance.mouseReleased(false)
    }
}