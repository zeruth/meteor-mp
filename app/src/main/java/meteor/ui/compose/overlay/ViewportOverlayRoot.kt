package meteor.ui.compose.overlay

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.meteor.android.MainActivity
import com.meteor.android.MainActivity.Companion.currentText
import com.meteor.android.MainActivity.Companion.focusRequester
import com.meteor.android.MainActivity.Companion.keyboardController
import com.meteor.android.MainActivity.Companion.showTextInput
import ext.compose.DrawScopeExt.dpToPx
import jagex2.client.Client
import jagex2.client.GameShell
import meteor.Main
import meteor.Main.forceRecomposition
import meteor.ui.compose.Colors
import meteor.ui.compose.components.GamePanel
import meteor.ui.compose.components.GamePanel.sX
import meteor.ui.compose.components.GamePanel.sY
import meteor.ui.compose.overlay.Overlay.Companion.debugOverlays
import meteor.ui.config.AspectMode
import net.runelite.api.Component
import java.awt.Dimension
import java.awt.Point

/**
 * This overlay is restricted to the viewport area
 */
object ViewportOverlayRoot {
    val VIEWPORT_DIMENSIONS = Dimension(512, 334)
    val VIEWPORT_OFFSETS = Point(8, 11)
    var yScale: Float? = null
    var xScale: Float? = null
    val polygons = HashMap<List<Point>, Color>()
    var width = mutableStateOf(Dp.Unspecified)
    var height = mutableStateOf(Dp.Unspecified)
    val canvasRenderTime = mutableStateOf(-1L)
    val viewportOverlays = ArrayList<ViewportOverlay>()
    var blockedViewportArea: Rect? = null
    var lastTextLength = 0

    @Composable
    fun render() {
        var mod = Modifier
            .absoluteOffset(x = 0.dp, y = 0.dp)
            .size(DpSize(1.dp, 1.dp))
            .clipToBounds()
            .background(Color.Transparent)

        if (Main.client.areaViewport != null) {
            forceRecomposition.value
            updateScale()

            var xScale = xScale;
            var yScale = yScale;
            if (Main.client.aspectMode == AspectMode.FIT) {
                if (yScale!! > xScale!!)
                    xScale = yScale
                else
                    yScale = xScale
            }

            val offsetX = (VIEWPORT_OFFSETS.x * sX).dp
            val offsetY = (VIEWPORT_OFFSETS.y * sY).dp

            width.value = (Main.client.areaViewport.image.getWidth(null) * sX).dp
            height.value = (Main.client.areaViewport.image.getHeight(null) * sY).dp

            if (width.value == 0.0.dp || height.value == 0.0.dp) {
                return
            }

            mod = Modifier
                .absoluteOffset(x = offsetX, y = offsetY)
                .size(DpSize(width.value, height.value))
                .clipToBounds()
                .background(Color.Transparent)
            if (/*Main.client.isLoggedIn() && */debugOverlays.value)
                mod = mod.background(Color.Red.copy(alpha = .2f))
        }

        Box(mod) {
            forceRecomposition.value
            DrawPolygons(mod)
            for (overlay in viewportOverlays) {
                overlay.render().invoke(this)
            }
            var mod = Modifier
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show() // Show keyboard when focused
                    }
                }.onKeyEvent { keyEvent ->
                    MainActivity.handleKeyEvent(keyEvent.nativeKeyEvent)
                }.fillMaxWidth()
            if (Client.client != null)
                if (!Main.client.isLoggedIn)
                    mod = mod.offset(x = 0.dp, y = (-1000).dp)
            if (showTextInput.value) {
                val view = LocalView.current
                val insets = ViewCompat.getRootWindowInsets(view)
                if (insets?.isVisible(WindowInsetsCompat.Type.ime()) == false) {
                    focusRequester.requestFocus()
                    keyboardController.show()
                }

                TextField(
                    value = currentText.value, // manage state here
                    onValueChange = {
                        currentText.value = it
                        if (it.length < lastTextLength) {
                            GameShell.keyPressed(8)
                            GameShell.keyReleased(8)
                        } else {
                            val lastChar = it.last()
                            println(it.last() + ":" + lastChar.code)
                            GameShell.keyPressed(lastChar.code)
                            GameShell.keyReleased(lastChar.code)
                        }
                        lastTextLength = it.length
                    },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false, // Disable autocorrect
                    ),
                    modifier = mod,
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Colors.secondary.value,
                        focusedIndicatorColor = Colors.secondary.value,
                        unfocusedContainerColor = Colors.surfaceDarker.value,
                        focusedContainerColor = Colors.surfaceDarker.value,
                        cursorColor = Colors.secondary.value,
                        focusedTextColor = Colors.secondary.value,
                        unfocusedTextColor = Colors.secondary.value),
                    placeholder = { Text("Press enter to close.") }
                )
            }
        }
    }

    @Composable
    fun DrawPolygons(mod: Modifier) {
        val textMeasurer = rememberTextMeasurer()
        Canvas(modifier = Modifier.fillMaxSize().background(Color.Transparent)) {
            //force recomposition on viewport overlays every frame.
            forceRecomposition.value
            val canvasRenderStart = System.currentTimeMillis()

            val blockPath = Path().apply {
                if (Main.client.viewportInterfaceID != -1) {
                    Main.client.components[Main.client.viewportInterfaceID]?.let {
                        addRect(calculateComponentArea(it))
                    }
                } else {
                    blockedViewportArea = null
                }
                if (Main.client.isMenuVisible) {
                    addRect(getScaledRect(Main.client.menuX.toFloat(), Main.client.menuY.toFloat(), Main.client.menuWidth.toFloat(), Main.client.menuHeight.toFloat()))
                }
            }
            clipPath(blockPath, clipOp = ClipOp.Difference) {
                for (overlay in viewportOverlays) {
                    overlay.draw(textMeasurer).invoke(this)
                }
                for (points in polygons.keys) {
                    drawPolygon(points, polygons[points]!!)
                }
            }

            polygons.clear()
            canvasRenderTime.value = System.currentTimeMillis() - canvasRenderStart
        }
    }

    @Composable
    fun testBox() {
        Box(modifier = Modifier.width(250.dp).height(75.dp).background(Color.Green)) {

        }
    }

    fun DrawScope.calculateComponentArea(component: Component): Rect {
        val x = component.x.toFloat()
        val y = component.y.toFloat()
        val w = component.width.toFloat()
        val h = component.height.toFloat()
        return getScaledRect(x, y, w, h)
    }

    fun DrawScope.getScaledRect(x: Float, y: Float, w: Float, h: Float): Rect {
        return Rect(
            Offset(dpToPx(x * xScale!!), dpToPx(y * yScale!!)),
            Offset(dpToPx((x + w) * xScale!!), dpToPx((y + h) * yScale!!))
        )
    }

    fun DrawScope.drawPolygon(points: List<Point>, color: Color) {
        if (points.size != 4)
            throw RuntimeException("Invalid args for drawPolygon")

        val x = dpToPx(points[0].x * xScale!!)
        val y = dpToPx(points[0].y * yScale!!)
        val x1 = dpToPx(points[1].x * xScale!!)
        val y1 = dpToPx(points[1].y * yScale!!)
        val x2 = dpToPx(points[2].x * xScale!!)
        val y2 = dpToPx(points[2].y * yScale!!)
        val x3 = dpToPx(points[3].x * xScale!!)
        val y3 = dpToPx(points[3].y * yScale!!)

        drawLine(start = Offset(x, y), end = Offset(x1, y1), color = color)
        drawLine(start = Offset(x, y), end = Offset(x2, y2), color = color)
        drawLine(start = Offset(x1, y1), end = Offset(x3, y3), color = color)
        drawLine(start = Offset(x2, y2), end = Offset(x3, y3), color = color)
    }


    fun updateScale() {
        xScale = GamePanel.sX
        yScale = GamePanel.sY
    }
}