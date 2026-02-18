package desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimary
import androidx.compose.ui.input.pointer.isSecondary
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import desktop.Main.focusRequester
import desktop.Main.panel
import desktop.Main.touchScaleX
import desktop.Main.touchScaleY
import jagex3.client.input.mouse.ClientMouseListener
import jagex3.util.MonotonicTime
import java.awt.Point
import java.awt.event.KeyEvent

object GameComposables {
    @OptIn(ExperimentalComposeUiApi::class)
    fun Modifier.registerInputListeners() : Modifier {
        return this.focusRequester(focusRequester).focusable(true)
            .onPointerEvent(PointerEventType.Move) { event ->
                val pointer = event.changes.firstOrNull() ?: return@onPointerEvent

                val x = pointer.position.x
                val y = pointer.position.y

                val scaled = Point(x.toInt(), y.toInt()).scaled()

                Main.invoke {
                    mouseMoved(scaled.x, scaled.y)
                }
            }
            .onPointerEvent(PointerEventType.Press) { event ->
                val pointer = event.changes.firstOrNull() ?: return@onPointerEvent
                val button = event.button ?: return@onPointerEvent

                val x = pointer.position.x
                val y = pointer.position.y

                val scaled = Point(x.toInt(), y.toInt()).scaled()

                if (button.isPrimary) {
                    Main.invoke {
                        mousePressed(scaled.x, scaled.y, 1)
                    }
                }
                else if (button.isSecondary) {
                    Main.invoke {
                        mousePressed(scaled.x, scaled.y, 2)
                    }
                }
            }
            .onPointerEvent(PointerEventType.Release) {
                Main.invoke {
                    mouseReleased()
                }
            }
            .onKeyEvent {
                it.awtEventOrNull?.let { awt ->
                    if (awt.id == KeyEvent.KEY_PRESSED) {
                        panel.keyPressed(awt)
                    }
                    if (awt.id == KeyEvent.KEY_TYPED) {
                        panel.keyTyped(awt)
                    }
                    if (awt.id == KeyEvent.KEY_RELEASED) {
                        panel.keyReleased(awt)
                    }
                }
                true
            }
    }

    @Composable
    fun Game() {
        SwingPanel(Color.Blue, modifier = Modifier.fillMaxSize().onGloballyPositioned { layoutCoordinates ->
            touchScaleX.floatValue = layoutCoordinates.size.width.toFloat() / 765
            touchScaleY.floatValue = layoutCoordinates.size.height.toFloat() / 503
        }, factory = {
            panel
        })
    }

    @Composable
    fun GameInputLayer() {
        Box(modifier = Modifier.fillMaxSize().background(Color.Transparent).registerInputListeners()) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }

    fun mouseMoved(x: Int, y: Int) {
        ClientMouseListener.idleTimer = 0
        ClientMouseListener.nextMouseX = x
        ClientMouseListener.nextMouseY = y
    }

    fun mousePressed(x: Int, y: Int, button: Int) {
        ClientMouseListener.idleTimer = 0
        ClientMouseListener.nextMouseClickX = x
        ClientMouseListener.nextMouseClickY = y
        ClientMouseListener.nextMouseClickTime = MonotonicTime.currentTime()
        ClientMouseListener.nextMouseClickButton = button
        ClientMouseListener.nextMouseButton = button
    }

    fun mouseReleased() {
        ClientMouseListener.idleTimer = 0
        ClientMouseListener.nextMouseButton = 0
    }

    private fun Point.scaled(): Point {
        if (touchScaleX.floatValue < 0f || touchScaleY.floatValue < 0f) {
            return Point(x, y)
        }

        val scaledX = (x / touchScaleX.floatValue).toInt()
        val scaledY = (y / touchScaleY.floatValue).toInt()

        return Point(scaledX, scaledY)
    }
}