package meteor.platform.android.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import meteor.platform.android.Game.image
import meteor.platform.common.Common.clientInstance
import meteor.platform.common.Common.eventbus
import meteor.platform.common.ui.UI.filterQuality
import meteor.platform.android.input.KeyboardController.handleKeyEvent
import meteor.platform.android.input.KeyboardController.keyboardController
import meteor.platform.android.ui.cameracontrols.CameraControls.CameraControls
import meteor.platform.android.ui.cameracontrols.CameraControls.resetKeys

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
    var touchScaleX = mutableFloatStateOf(0f)
    var touchScaleY = mutableFloatStateOf(0f)
    var mouseDown = false
    var waitFrame = 0
    var waitTapFrame = 0

    init {
        eventbus.subscribe<client.events.DrawFinished>(priority = Int.MAX_VALUE) {
            if (mouseDown)
                waitFrame += 1
            else
                waitFrame = 0

            pendingTap?.let {
                waitTapFrame += 1
            }
        }
        eventbus.subscribe<client.events.DrawFinished> {
            pendingMove?.let {
                clientInstance.`mouseMoved$api`(it.x, it.y)
                mouseDown = true
                pendingMove = null
            }
        }
        eventbus.subscribe<client.events.DrawFinished> {
            pendingPress?.let {
                if (!mouseDown || waitFrame == 0)
                    return@let
                clientInstance.`mousePressed$api`(it.x, it.y, 1, false)
                pendingPress = null
            }
        }
        eventbus.subscribe<client.events.DrawFinished> {
            pendingTap?.let {
                if (waitTapFrame == 0)
                    return@let
                clientInstance.`mousePressed$api`(it.x, it.y, 1, false)
                clientInstance.`mouseReleased$api`(1, false)
                pendingTap = null
            }
        }
        eventbus.subscribe<client.events.DrawFinished> {
            pendingHold?.let {
                if (!mouseDown || waitFrame == 0)
                    return@let
                clientInstance.`mousePressed$api`(it.x, it.y, 3, false)
                clientInstance.`mouseReleased$api`(3, false)
                pendingHold = null
            }
        }
        eventbus.subscribe<client.events.AreaViewportDrawFinished> {
            viewportImage.value = Bitmap.createBitmap(clientInstance.areaViewport.pixels, clientInstance.areaViewport.width, clientInstance.areaViewport.height, Bitmap.Config.RGB_565).asImageBitmap()
        }
        eventbus.subscribe<client.events.DrawFinished> {
            if (sceneState.intValue != clientInstance.sceneState)
                sceneState.intValue = clientInstance.sceneState
        }
    }
    var viewportImage = mutableStateOf<ImageBitmap?>(null)

    private fun android.graphics.Point.scaled(): android.graphics.Point {
        val scaledX = (x / touchScaleX.floatValue).toInt()
        val scaledY = (y / touchScaleY.floatValue).toInt()

        return android.graphics.Point(scaledX, scaledY);
    }

    val volumeKeys = listOf(Key.VolumeUp,  Key.VolumeDown, Key.VolumeMute)

    fun KeyEvent.isVolumeKey(): Boolean {
        return volumeKeys.contains(this.key)
    }

    fun Modifier.registerKeyListener(): Modifier {
        return this.onKeyEvent { keyEvent: KeyEvent ->
            if (keyEvent.isVolumeKey())
                return@onKeyEvent false
            if (keyEvent.type == KeyEventType.KeyDown) {
                if (keyEvent.key == Key.DirectionLeft) {
                    clientInstance.`keyPressed$api`(java.awt.event.KeyEvent.VK_LEFT, -1)
                }
                else if (keyEvent.key == Key.DirectionRight) {
                    clientInstance.`keyPressed$api`(java.awt.event.KeyEvent.VK_RIGHT, -1)
                }
                else if (keyEvent.key == Key.DirectionUp) {
                    clientInstance.`keyPressed$api`(java.awt.event.KeyEvent.VK_UP, -1)
                }
                else if (keyEvent.key == Key.DirectionDown) {
                    clientInstance.`keyPressed$api`(java.awt.event.KeyEvent.VK_DOWN, -1)
                }
                else
                    handleKeyEvent(keyEvent.nativeKeyEvent)
            }
            else if (keyEvent.type == KeyEventType.KeyUp) {
                if (keyEvent.key == Key.DirectionLeft) {
                    clientInstance.`keyReleased$api`(java.awt.event.KeyEvent.VK_LEFT)
                }
                else if (keyEvent.key == Key.DirectionRight) {
                    clientInstance.`keyReleased$api`(java.awt.event.KeyEvent.VK_RIGHT)
                }
                else if (keyEvent.key == Key.DirectionUp) {
                    clientInstance.`keyReleased$api`(java.awt.event.KeyEvent.VK_UP)
                }
                else if (keyEvent.key == Key.DirectionDown) {
                    clientInstance.`keyReleased$api`(java.awt.event.KeyEvent.VK_DOWN)
                }
                else
                    handleKeyEvent(keyEvent.nativeKeyEvent)
            }
            else
                handleKeyEvent(keyEvent.nativeKeyEvent)

            true
        }
    }

    fun drawViewportOntoImage(sourceBitmap: Bitmap, targetBitmap: Bitmap): Bitmap {
        val resultBitmap = targetBitmap.copy(Bitmap.Config.RGB_565, true)
        val canvas = Canvas(resultBitmap)
        val paint = Paint()
        canvas.drawBitmap(sourceBitmap, 8f, 11f, paint)
        return resultBitmap
    }

    lateinit var gamePanelFocusRequester: FocusRequester
    val gameInputText = mutableStateOf("")
    val hideInputBox = mutableStateOf(false)
    val sceneState = mutableIntStateOf(-1)

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Game() {
        var clickCoordinates by remember { mutableStateOf<Offset?>(null) }
        // Get the screen density
        density = LocalDensity.current.density
        var longPressing = false


        Box(modifier = Modifier.fillMaxSize()) {
            image.value?.let {
                var it = it
                if (clientInstance.inGame()) {
                    if (sceneState.intValue > 1) {
                        viewportImage.value?.let { image ->
                            it = drawViewportOntoImage(image.asAndroidBitmap(), it.asAndroidBitmap()).asImageBitmap()
                        }
                    }
                }

                val interactionSource = remember { MutableInteractionSource() }
                gamePanelFocusRequester = remember { FocusRequester() }
                Image(it, "", filterQuality = filterQuality.value.composeValue, contentScale = ContentScale.FillBounds, modifier =
                Modifier
                    .focusRequester(gamePanelFocusRequester) // Request focus for this field
                    .onFocusChanged { focusState ->
                        // Handle focus state changes if needed
                    }.fillMaxSize()
                    .focusable(true, interactionSource)
                    .onKeyEvent { keyEvent ->
                        handleKeyEvent(keyEvent.nativeKeyEvent)
                        false
                    }
                    .registerKeyListener()
                    .onGloballyPositioned { layoutCoordinates ->
                        containerSize.value = layoutCoordinates.size
                        touchScaleX.value = containerSize.value.width.toFloat() / 789
                        touchScaleY.value = containerSize.value.height.toFloat() / 532
                    }.pointerInteropFilter { change ->
                        resetKeys()
                        clientInstance.`mouseMoved$api`((change.x / touchScaleX.value).toInt(), (change.y / touchScaleY.value).toInt())
                        false
                    }.pointerInput(Unit) {
                        detectDragGestures(onDragStart = {
                            pendingMove = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                            pendingPress = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                        }, onDragCancel = {
                            clientInstance.`mouseReleased$api`()
                        }, onDragEnd = {
                            clientInstance.`mouseReleased$api`()
                        }) { change, dragAmount ->
                            pendingMove = android.graphics.Point(change.position.x.toInt(), change.position.y.toInt()).scaled()
                        }
                    }.pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            pendingMove = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                            pendingTap = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                        }, onLongPress = {
                            pendingMove = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                            pendingHold = android.graphics.Point(it.x.toInt(), it.y.toInt()).scaled()
                        })
                    })
            }
            CameraControls()

            /**
             * TODO: This is a cheeky workaround for showing the keyboard
             */
            if (!hideInputBox.value) {
                BasicTextField(
                    value = "",
                    onValueChange = { },
                    keyboardOptions = KeyboardOptions.Default.copy(),
                    keyboardActions = KeyboardActions(),
                    modifier = Modifier
                        // This is how we trick it into showing keyboard the first time
                        .focusRequester(gamePanelFocusRequester)
                )
            }
        }
    }
}