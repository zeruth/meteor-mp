package meteor.ui.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.meteor.android.MainActivity.Companion.image
import com.meteor.android.MainActivity.Companion.viewportImage
import jagex2.client.Client
import jagex2.client.GameShell
import meteor.Constants
import meteor.Main
import meteor.Main.forceRecomposition
import meteor.events.DrawFinished
import meteor.ui.compose.overlay.GameOverlayRoot
import meteor.ui.config.AspectMode
import org.rationalityfrontline.kevent.KEVENT
import java.awt.Point

/**
 * This panel will contain the game view & compose overlays eventually
 */
object GamePanel {
    var aspectMode = mutableStateOf(Main.client.aspectMode)
    var pendingMove : android.graphics.Point? = null
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
    var containerSize = mutableStateOf(IntSize(Constants.RS_DIMENSIONS.width, Constants.RS_DIMENSIONS.height))
    val dragging = mutableStateOf(false)
    val filter = mutableStateOf(FilterQuality.Medium)
    var density = 1f
    init {
        KEVENT.subscribe<DrawFinished> {
            pendingMove?.let {
                GameShell.mouseMoved(it.x, it.y)
                pendingMove = null
                return@subscribe
            }
            pendingTap?.let {
                GameShell.mousePressed(it.x, it.y, 1)
                GameShell.mouseReleased(1)
                pendingTap = null
                return@subscribe
            }
            pendingHold?.let {
                GameShell.mousePressed(it.x, it.y, 2)
                GameShell.mouseReleased(2)
                pendingHold = null
                return@subscribe
            }
        }
    }
    var touchScaleX = 0f
    var touchScaleY = 0f

    private fun createScaledPoint(x: Float, y: Float): Point {
        val scaledX = x / touchScaleX
        val scaledY = y / touchScaleY

        return Point(scaledX.toInt(), scaledY.toInt());
    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Game() {
        var clickCoordinates by remember { mutableStateOf<Offset?>(null) }
        // Get the screen density
        density = LocalDensity.current.density
        val xPad = ((containerSize.value.width.toFloat() / density) - (Constants.RS_DIMENSIONS.width * sX)) / 2
        val yPad = ((containerSize.value.height.toFloat() / density) - (Constants.RS_DIMENSIONS.height * sY)) / 2
        val viewportXOffset = (8 * sX) + xPad
        val viewportYOffset = (11 * sY) + yPad
        Box(modifier = Modifier.fillMaxSize().pointerInteropFilter { pointerInputChange ->
            val point = createScaledPoint(pointerInputChange.x, pointerInputChange.y)
            // Call your method to handle the hover event
            GameShell.mouseMoved(point.x, point.y)
            // Allow the event to be consumed
            false
        }) {
            image.value?.let {
                Image(it, "", filterQuality = filter.value, contentScale = if (aspectMode.value == AspectMode.FIT) ContentScale.Fit else ContentScale.FillBounds, modifier =
                Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { layoutCoordinates ->
                        containerSize.value = layoutCoordinates.size
                        updateScale(containerSize.value, density)
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(onDragStart = {
                            val downLocation = createScaledPoint(it.x, it.y)
                            GameShell.mouseMoved(downLocation.x, downLocation.y)
                            GameShell.mousePressed(downLocation.x, downLocation.y, 1)
                        },
                            onDragEnd = {
                                GameShell.mouseReleased(1)
                            },
                            onDragCancel = {
                                GameShell.mouseReleased(1)
                            }) { change, _ ->
                            val moveLocation = createScaledPoint(change.position.x, change.position.y)
                            GameShell.mouseMoved(moveLocation.x, moveLocation.y)
                        }
                    }.pointerInput(Unit) {
                        detectTapGestures(onTap =  { offset ->
                            touchScaleX = containerSize.value.width.toFloat() / Constants.RS_DIMENSIONS.width
                            touchScaleY = containerSize.value.height.toFloat() / Constants.RS_DIMENSIONS.height

                            val fitScaleFactor = when (aspectMode.value) {
                                AspectMode.FIT -> minOf(touchScaleX, touchScaleY)
                                AspectMode.FILL -> maxOf(touchScaleX, touchScaleY)
                            }

                            if (aspectMode.value == AspectMode.FIT) {
                                if (offset.x > halfXPadding.floatValue)
                                    if (offset.x < (halfXPadding.floatValue + stretchedWidth.floatValue)) {
                                        var modX =
                                            if (halfXPadding.floatValue > 0) (offset.x - halfXPadding.floatValue) else offset.x
                                        modX /= fitScaleFactor
                                        if (offset.y > halfYPadding.floatValue)
                                            if (offset.y < (halfYPadding.floatValue + stretchedHeight.floatValue)) {
                                                var modY =
                                                    if (halfYPadding.floatValue > 0) (offset.y - halfYPadding.floatValue) else offset.y
                                                modY /= fitScaleFactor
                                                pendingMove =
                                                    android.graphics.Point(modX.toInt(), modY.toInt())
                                                pendingTap =
                                                    android.graphics.Point(modX.toInt(), modY.toInt())
                                            }
                                    }
                            } else {
                                val modX = offset.x / touchScaleX
                                val modY = offset.y / touchScaleY
                                pendingMove = android.graphics.Point(modX.toInt(), modY.toInt())
                                pendingTap = android.graphics.Point(modX.toInt(), modY.toInt())
                            }
                        }, onLongPress = {
                                offset ->
                            touchScaleX = containerSize.value.width.toFloat() / Constants.RS_DIMENSIONS.width
                            touchScaleY = containerSize.value.height.toFloat() / Constants.RS_DIMENSIONS.height
                            if (dragging.value)
                                return@detectTapGestures
                            if (aspectMode.value == AspectMode.FIT) {
                                if (offset.x > halfXPadding.floatValue)
                                    if (offset.x < (halfXPadding.floatValue + stretchedWidth.floatValue)) {
                                        var modX =
                                            if (halfXPadding.floatValue > 0) (offset.x - halfXPadding.floatValue) else offset.x
                                        modX /= fitScaleFactor.floatValue
                                        if (offset.y > halfYPadding.floatValue)
                                            if (offset.y < (halfYPadding.floatValue + stretchedHeight.floatValue)) {
                                                var modY =
                                                    if (halfYPadding.floatValue > 0) (offset.y - halfYPadding.floatValue) else offset.y
                                                modY /= fitScaleFactor.floatValue
                                                pendingMove =
                                                    android.graphics.Point(modX.toInt(), modY.toInt())
                                                pendingHold =
                                                    android.graphics.Point(modX.toInt(), modY.toInt())
                                            }
                                    }
                            } else {
                                val modX = offset.x / touchScaleX
                                val modY = offset.y / touchScaleY
                                pendingMove = android.graphics.Point(modX.toInt(), modY.toInt())
                                pendingHold = android.graphics.Point(modX.toInt(), modY.toInt())
                            }
                        })
                    })
            }
        }

        if (Main.client.isLoggedIn && Main.client.areaViewport != null)
            viewportImage.value?.let {
                Box(modifier = Modifier.offset(x = viewportXOffset.dp, y = viewportYOffset.dp).size((Main.client.areaViewport.image.getWidth(null) * sX).dp, (Main.client.areaViewport.image.getHeight(null) * sY).dp).fillMaxSize().pointerInput(Unit) {
                    detectDragGestures(onDragStart = {
                            dragging.value = true
                    },
                        onDragEnd = {
                            dragging.value = false
                        },
                        onDragCancel = {
                            dragging.value = false
                        }) { _, _ -> }
                }.pointerInput(Unit) {
                    detectDragGestures(onDragStart = {
                        if (Main.interfaceOpen.value) {
                            val downLocation = createScaledPoint(it.x, it.y)
                            GameShell.mouseMoved(downLocation.x, downLocation.y)
                            GameShell.mousePressed(downLocation.x, downLocation.y, 1)
                        }
                    },
                        onDragEnd = {
                            if (Main.interfaceOpen.value)
                                GameShell.mouseReleased(1)
                        },
                        onDragCancel = {
                            if (Main.interfaceOpen.value)
                                GameShell.mouseReleased(1)
                        }) { change, dragAmount ->
                        if (!Main.interfaceOpen.value) {
                            val cameraYawChange = change.positionChange().x
                            Main.client.cameraYaw -= cameraYawChange.toInt()
                            Main.client.cameraYaw = Main.client.cameraYaw and 0x7FF

                            val cameraPitchChange = change.positionChange().y
                            Main.client.cameraPitch += cameraPitchChange.toInt()
                            if (Main.client.cameraPitch < 128) {
                                Main.client.cameraPitch = 128
                            }
                            if (Main.client.cameraPitch > 383) {
                                Main.client.cameraPitch = 383
                            }
                        } else {
                            val moveLocation = createScaledPoint(change.position.x, change.position.y)
                            GameShell.mouseMoved(moveLocation.x, moveLocation.y)
                        }
                    }
                }) {
                    var viewportMod = Modifier.fillMaxSize()
                    Image(it, "", filterQuality = filter.value, contentScale = ContentScale.FillBounds, modifier = viewportMod.pointerInteropFilter { pointerInputChange ->
                        val point = createScaledPoint(pointerInputChange.x, pointerInputChange.y)
                        // Call your method to handle the hover event
                        GameShell.mouseMoved(point.x + 8, point.y + 11)
                        // Allow the event to be consumed
                        false
                    }.pointerInput(Unit) {
                        detectTapGestures(onTap =  { offset ->
                            touchScaleX = containerSize.value.width.toFloat() / Constants.RS_DIMENSIONS.width
                            touchScaleY = containerSize.value.height.toFloat() / Constants.RS_DIMENSIONS.height

                            val fitScaleFactor = when (aspectMode.value) {
                                AspectMode.FIT -> minOf(touchScaleX, touchScaleY)
                                AspectMode.FILL -> maxOf(touchScaleX, touchScaleY)
                            }

                            if (aspectMode.value == AspectMode.FIT) {
                                if (offset.x > halfXPadding.floatValue)
                                    if (offset.x < (halfXPadding.floatValue + stretchedWidth.floatValue)) {
                                        var modX =
                                            if (halfXPadding.floatValue > 0) (offset.x - halfXPadding.floatValue) else offset.x
                                        modX /= fitScaleFactor
                                        if (offset.y > halfYPadding.floatValue)
                                            if (offset.y < (halfYPadding.floatValue + stretchedHeight.floatValue)) {
                                                var modY =
                                                    if (halfYPadding.floatValue > 0) (offset.y - halfYPadding.floatValue) else offset.y
                                                modY /= fitScaleFactor
                                                pendingMove =
                                                    android.graphics.Point(modX.toInt(), modY.toInt())
                                                pendingTap =
                                                    android.graphics.Point(modX.toInt(), modY.toInt())
                                            }
                                    }
                            } else {
                                val modX = offset.x / touchScaleX + 8
                                val modY = offset.y / touchScaleY + 11
                                pendingMove = android.graphics.Point(modX.toInt(), modY.toInt())
                                pendingTap = android.graphics.Point(modX.toInt(), modY.toInt())
                            }
                        }, onLongPress = {
                                offset ->
                            touchScaleX = containerSize.value.width.toFloat() / Constants.RS_DIMENSIONS.width
                            touchScaleY = containerSize.value.height.toFloat() / Constants.RS_DIMENSIONS.height
                            if (dragging.value)
                                return@detectTapGestures
                            if (aspectMode.value == AspectMode.FIT) {
                                if (offset.x > halfXPadding.floatValue)
                                    if (offset.x < (halfXPadding.floatValue + stretchedWidth.floatValue)) {
                                        var modX =
                                            if (halfXPadding.floatValue > 0) (offset.x - halfXPadding.floatValue) else offset.x
                                        modX /= fitScaleFactor.floatValue
                                        if (offset.y > halfYPadding.floatValue)
                                            if (offset.y < (halfYPadding.floatValue + stretchedHeight.floatValue)) {
                                                var modY =
                                                    if (halfYPadding.floatValue > 0) (offset.y - halfYPadding.floatValue) else offset.y
                                                modY /= fitScaleFactor.floatValue
                                                pendingMove =
                                                    android.graphics.Point(modX.toInt(), modY.toInt())
                                                pendingHold =
                                                    android.graphics.Point(modX.toInt(), modY.toInt())
                                            }
                                    }
                            } else {
                                val modX = offset.x / touchScaleX
                                val modY = offset.y / touchScaleY
                                pendingMove = android.graphics.Point(modX.toInt(), modY.toInt())
                                pendingHold = android.graphics.Point(modX.toInt(), modY.toInt())
                            }
                        })
                    })
                }

            }
        GameOverlayRoot.render()
    }

    fun updateScale(containerSize: IntSize, density: Float) {
        val imageSize =
            IntSize(Client.frame.width, Client.frame.height)
        // Convert image size from pixels to dp
        val imageWidthDp = imageSize.width
        val imageHeightDp = imageSize.height

        // Convert container size from pixels to dp
        val containerWidthDp = containerSize.width / density
        val containerHeightDp = containerSize.height / density

        // Calculate scale
        sX = containerWidthDp / imageWidthDp
        sY = containerHeightDp / imageHeightDp

        fitScaleFactor.value = when (aspectMode.value) {
            AspectMode.FIT -> minOf(sX, sY)
            AspectMode.FILL -> maxOf(sX, sY)
        }

        if (aspectMode.value == AspectMode.FIT) {
            println("scaleX: ${sX} scaleY: ${sY} factor: ${fitScaleFactor}")

            stretchedWidth.floatValue = Constants.RS_DIMENSIONS.width * fitScaleFactor.floatValue
            xPadding.floatValue = containerSize.width - stretchedWidth.floatValue
            halfXPadding.floatValue = xPadding.floatValue / 2

            stretchedHeight.floatValue = Constants.RS_DIMENSIONS.height * fitScaleFactor.floatValue
            yPadding.floatValue = containerSize.height - stretchedHeight.floatValue
            halfYPadding.floatValue = yPadding.floatValue / 2
        }
    }
}