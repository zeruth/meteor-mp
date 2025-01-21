package meteor.ui.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.meteor.android.MainActivity
import com.meteor.android.MainActivity.Companion.clientInstance
import com.meteor.android.MainActivity.Companion.fps
import com.meteor.android.MainActivity.Companion.image
import meteor.Common.filterQuality
import meteor.Main
import meteor.events.DrawFinished
import org.rationalityfrontline.kevent.KEVENT
import java.awt.Point
import java.lang.Math.abs

/**
 * This panel will contain the game view & compose overlays eventually
 */
object GamePanel {
    var pendingMove : android.graphics.Point? = null
    var pendingPress : android.graphics.Point? = null
    var pendingTap : android.graphics.Point? = null
    var pendingHold : android.graphics.Point? = null

    var sX = 0f
    var sY = 0f
    var stretchedWidth = mutableFloatStateOf(0f)
    var stretchedHeight = mutableFloatStateOf(0f)
    var xPadding = mutableFloatStateOf(0f)
    var yPadding = mutableFloatStateOf(0f)
    var halfXPadding = mutableFloatStateOf(0f)
    var halfYPadding = mutableFloatStateOf(0f)
    var fitScaleFactor = mutableFloatStateOf(0f)
    var containerSize = mutableStateOf(IntSize(789, 532))
    val dragging = mutableStateOf(false)
    val filter = mutableStateOf(FilterQuality.Medium)
    var density = 1f
    var touchScaleX = 0f
    var touchScaleY = 0f
    var mouseDown = false
    var waitFrame = 0
    var waitTapFrame = 0

    init {
        KEVENT.subscribe<client.events.DrawFinished>(priority = Int.MAX_VALUE) {
            if (mouseDown)
                waitFrame += 1
            else
                waitFrame = 0

            pendingTap?.let {
                waitTapFrame += 1
            }
        }
        KEVENT.subscribe<client.events.DrawFinished> {
            pendingMove?.let {
                clientInstance.mouseMoved(it.x, it.y)
                mouseDown = true
                pendingMove = null
            }
        }
        KEVENT.subscribe<client.events.DrawFinished> {
            pendingPress?.let {
                if (!mouseDown || waitFrame == 0)
                    return@let
                clientInstance.mousePressed(it.x, it.y, 1, false)
                pendingPress = null
            }
        }
        KEVENT.subscribe<client.events.DrawFinished> {
            pendingTap?.let {
                if (waitTapFrame == 0)
                    return@let
                clientInstance.mousePressed(it.x, it.y, 1, false)
                clientInstance.mouseReleased(1, false)
                pendingTap = null
            }
        }
        KEVENT.subscribe<client.events.DrawFinished> {
            pendingHold?.let {
                if (!mouseDown || waitFrame == 0)
                    return@let
                clientInstance.mousePressed(it.x, it.y, 3, false)
                clientInstance.mouseReleased(3, false)
                pendingHold = null
            }
        }
    }

    private fun android.graphics.Point.scaled(): android.graphics.Point {
        val scaledX = (x / touchScaleX).toInt()
        val scaledY = (y / touchScaleY).toInt()

        return android.graphics.Point(scaledX, scaledY);
    }

    fun Modifier.registerKeyListener(): Modifier {
        return this.onKeyEvent { keyEvent: KeyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                if (keyEvent.key == Key.DirectionLeft) {
                    clientInstance.keyPressed(java.awt.event.KeyEvent.VK_LEFT, -1)
                }
                else if (keyEvent.key == Key.DirectionRight) {
                    clientInstance.keyPressed(java.awt.event.KeyEvent.VK_RIGHT, -1)
                }
                else if (keyEvent.key == Key.DirectionUp) {
                    clientInstance.keyPressed(java.awt.event.KeyEvent.VK_UP, -1)
                }
                else if (keyEvent.key == Key.DirectionDown) {
                    clientInstance.keyPressed(java.awt.event.KeyEvent.VK_DOWN, -1)
                }
                else
                    MainActivity.handleKeyEvent(keyEvent.nativeKeyEvent)
            }
            else if (keyEvent.type == KeyEventType.KeyUp) {
                if (keyEvent.key == Key.DirectionLeft) {
                    clientInstance.keyReleased(java.awt.event.KeyEvent.VK_LEFT)
                }
                else if (keyEvent.key == Key.DirectionRight) {
                    clientInstance.keyReleased(java.awt.event.KeyEvent.VK_RIGHT)
                }
                else if (keyEvent.key == Key.DirectionUp) {
                    clientInstance.keyReleased(java.awt.event.KeyEvent.VK_UP)
                }
                else if (keyEvent.key == Key.DirectionDown) {
                    clientInstance.keyReleased(java.awt.event.KeyEvent.VK_DOWN)
                }
                else
                    MainActivity.handleKeyEvent(keyEvent.nativeKeyEvent)
            }
            else
                MainActivity.handleKeyEvent(keyEvent.nativeKeyEvent)

            true
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Game() {
        var clickCoordinates by remember { mutableStateOf<Offset?>(null) }
        // Get the screen density
        density = LocalDensity.current.density
        var longPressing = false

        Box(modifier = Modifier.fillMaxSize()) {
            image.value?.let {
                val interactionSource = remember { MutableInteractionSource() }
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                Image(it, "", filterQuality = filterQuality.value.composeValue, contentScale = ContentScale.FillBounds, modifier =
                Modifier
                    .fillMaxSize()
                    .focusable(true, interactionSource)
                    .focusRequester(focusRequester)
                    .onKeyEvent { keyEvent ->
                        MainActivity.handleKeyEvent(keyEvent.nativeKeyEvent)
                    }
                    .registerKeyListener()
                    .onGloballyPositioned { layoutCoordinates ->
                        containerSize.value = layoutCoordinates.size
                        touchScaleX = containerSize.value.width.toFloat() / 789
                        touchScaleY = containerSize.value.height.toFloat() / 532
                    }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(onDragStart = {

                        }, onDragEnd = {
                            mouseDown = false
                        }) { change, dragAmount ->

                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(onDragStart = {
                            pendingMove = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                            pendingPress = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                        }, onDragEnd = {
                            clientInstance.mouseReleased()
                            mouseDown = false
                        }) { change, _ ->
                            pendingMove = android.graphics.Point(change.position.x.toInt(), change.position.y.toInt()).scaled()
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            pendingMove = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                            pendingTap = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                        }, onLongPress = {
                            pendingMove = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                            pendingHold = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                        })

                    })
            }
            Text("FPS: ${fps.intValue}", color = Color.Yellow, modifier = Modifier.align(Alignment.TopEnd))
        }
    }
}