package meteor.platform.desktop.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import client.events.DrawFinished
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CrossSolid
import compose.icons.lineawesomeicons.WindowCloseSolid
import jagex2.client.GameShellMapView
import mapview
import meteor.platform.common.ext.kotlin.MutableStateExt.toggle
import meteor.platform.common.ui.Colors
import org.rationalityfrontline.kevent.KEVENT

object MapView {
    val mapVisible = mutableStateOf(false)
    val mapLoaded = mutableStateOf(false)
    var mapPendingStart = false
    val updateImage = mutableStateOf(false)
    lateinit var mapview: mapview
    init {
        KEVENT.subscribe<DrawFinished> {
            if (mapPendingStart) {
                mapPendingStart = false
                mapview = mapview()
                mapview.init()
                mapview.load()
                mapLoaded.value = true
            }
            updateImage.toggle()
        }
    }

    var mapScaleX = 0f
    var mapScaleY = 0f

    val undecorated = mutableStateOf(true)
    val focusRequester = FocusRequester()

    @Composable
    fun MapView() {
        val density = LocalDensity.current
        val mapWidthAdjusted = (635 * density.density).toInt()
        val mapHeightAdjusted = (503 * density.density).toInt()

        key(mapVisible.value) {
            key(mapLoaded.value) {
                if (mapVisible.value && !mapLoaded.value)
                    mapPendingStart = true
                if (mapVisible.value && mapLoaded.value) {
                    Window({}, state = WindowState(WindowPlacement.Maximized,
                        false, WindowPosition.Aligned(Alignment.Center)),
                        undecorated = undecorated.value, transparent = undecorated.value, resizable = true) {
                        if (!this.window.windowListeners.contains(mapview))
                            this.window.addWindowListener(mapview)
                        Box(modifier = Modifier.focusable().fillMaxSize().focusRequester(focusRequester).registerDragListener().registerLeftClickListener().registerMouseMoveListener().onSizeChanged {
                            mapScaleX = (635f / it.width)
                            mapScaleY = (503f / it.height)
                        }) {
                            key(updateImage.value) {
                                Image(GameShellMapView.image.toComposeImageBitmap(), "map", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
                            }
                            Image(LineAwesomeIcons.WindowCloseSolid, "Exit", colorFilter = ColorFilter.tint(
                                Color.Red), modifier = Modifier.size(50.dp).background(Colors.surface.value).clip(RoundedCornerShape(10.dp)).align(Alignment.TopEnd).clickable {
                                mapVisible.value = false
                            })
                        }
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    }
                }
            }
        }
    }

    fun Modifier.registerDragListener(): Modifier {
        return this.pointerInput(Unit) {
            detectDragGestures(onDragStart = {
                sendLeftClick(it.x.toInt(), it.y.toInt(), false)
            }, onDragCancel = {
                mapview.mouseReleased(false)
            }, onDragEnd = {
                mapview.mouseReleased(false)
            }) { change, dragAmount ->
                sendMouseMove(change.position.x.toInt(), change.position.y.toInt())
            }
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

    fun sendMouseMove(x: Int, y: Int) {
        val offset = Offset(x.toFloat(), y.toFloat())
        val adjustedX = (offset.x * mapScaleX)
        val adjustedY = (offset.y * mapScaleY)
        mapview.mouseMoved(adjustedX.toInt(), adjustedY.toInt())
    }

    fun sendLeftClick(x: Int, y: Int, release: Boolean = true) {
        println("Lclick-pre")
        val adjustedX = (x * mapScaleX)
        val adjustedY = (y * mapScaleY)
        mapview.mouseMoved(adjustedX.toInt(), adjustedY.toInt())
        mapview.mousePressed(adjustedX.toInt(), adjustedY.toInt(), 1, false)
        if (release)
            mapview.mouseReleased(false)
    }
}