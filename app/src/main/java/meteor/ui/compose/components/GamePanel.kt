package meteor.ui.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.meteor.android.MainActivity.Companion.image
import jagex2.client.GameShell
import meteor.Constants
import meteor.Main
import meteor.events.DrawFinished
import meteor.ui.compose.overlay.GameOverlayRoot
import meteor.ui.config.AspectMode
import org.rationalityfrontline.kevent.KEVENT

/**
 * This panel will contain the game view & compose overlays eventually
 */
object GamePanel {
    var aspectMode = mutableStateOf(Main.client.aspectMode)
    var pendingMove : android.graphics.Point? = null
    var pendingTap : android.graphics.Point? = null
    var pendingHold : android.graphics.Point? = null
    var scaleX = 0f
    var scaleY = 0f
    var stretchedWidth = mutableFloatStateOf(0f)
    var stretchedHeight = mutableFloatStateOf(0f)
    var xPadding = mutableFloatStateOf(0f)
    var yPadding = mutableFloatStateOf(0f)
    var halfXPadding = mutableFloatStateOf(0f)
    var halfYPadding = mutableFloatStateOf(0f)
    var fitScaleFactor = mutableFloatStateOf(0f)
    var containerSize = mutableStateOf(IntSize(Constants.RS_DIMENSIONS.width, Constants.RS_DIMENSIONS.height))
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
    @Composable
    fun Game() {
        var clickCoordinates by remember { mutableStateOf<Offset?>(null) }

        image.value?.let {
            Image(it, "", filterQuality = FilterQuality.None, contentScale = if (aspectMode.value == AspectMode.FIT) ContentScale.Fit else ContentScale.FillBounds, modifier =
            Modifier
                .fillMaxSize()
                .onGloballyPositioned { layoutCoordinates ->
                    containerSize.value = layoutCoordinates.size
                    updateScale(containerSize.value)
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
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
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(onTap =  { offset ->
                        val imageSize =
                            IntSize(Constants.RS_DIMENSIONS.width, Constants.RS_DIMENSIONS.height)
                        scaleX = containerSize.value.width.toFloat() / imageSize.width
                        scaleY = containerSize.value.height.toFloat() / imageSize.height

                        val fitScaleFactor = when (aspectMode.value) {
                            AspectMode.FIT -> minOf(scaleX, scaleY)
                            AspectMode.FILL -> maxOf(scaleX, scaleY)
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
                            val modX = offset.x / scaleX
                            val modY = offset.y / scaleY
                            pendingMove = android.graphics.Point(modX.toInt(), modY.toInt())
                            pendingTap = android.graphics.Point(modX.toInt(), modY.toInt())
                        }
                    }, onLongPress = {
                            offset ->
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
                            val modX = offset.x / scaleX
                            val modY = offset.y / scaleY
                            pendingMove = android.graphics.Point(modX.toInt(), modY.toInt())
                            pendingHold = android.graphics.Point(modX.toInt(), modY.toInt())
                        }
                    })
                })
        }
        GameOverlayRoot.render()
    }

    fun updateScale(containerSize: IntSize) {
        val imageSize =
            IntSize(Constants.RS_DIMENSIONS.width, Constants.RS_DIMENSIONS.height)
        scaleX = containerSize.width.toFloat() / imageSize.width
        scaleY = containerSize.height.toFloat() / imageSize.height

        fitScaleFactor.value = when (aspectMode.value) {
            AspectMode.FIT -> minOf(scaleX, scaleY)
            AspectMode.FILL -> maxOf(scaleX, scaleY)
        }

        if (aspectMode.value == AspectMode.FIT) {
            if (scaleX > scaleY)
                scaleY = scaleX
            if (scaleY > scaleX)
                scaleX = scaleY
            println("scaleX: ${scaleX} scaleY: ${scaleY} factor: ${fitScaleFactor}")

            stretchedWidth.floatValue = Constants.RS_DIMENSIONS.width * fitScaleFactor.floatValue
            xPadding.floatValue = containerSize.width - stretchedWidth.floatValue
            halfXPadding.floatValue = xPadding.floatValue / 2

            stretchedHeight.floatValue = Constants.RS_DIMENSIONS.height * fitScaleFactor.floatValue
            yPadding.floatValue = containerSize.height - stretchedHeight.floatValue
            halfYPadding.floatValue = yPadding.floatValue / 2
        }
    }
}