package meteor

import android.graphics.Paint
import android.graphics.Point
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import jagex2.client.Client
import jagex2.client.GameShell
import meteor.context.events.AndroidPixMapDraw
import meteor.context.events.Draw
import meteor.context.platform.android.AndroidPlatform
import meteor.context.platform.android.AndroidViewBox
import util.GlobalEventBus

class MainActivity : ComponentActivity() {
    val paint = Paint()
    var loaded = false

    val state = mutableStateOf(false)
    var mouseDown = false
    var waitFrame = 0
    var waitTapFrame = 0
    var pendingMove : Point? = null
    var pendingPress : Point? = null
    var pendingTap : Point? = null
    var pendingHold : Point? = null
    var containerSize = mutableStateOf(IntSize(-1, -1))
    var touchScaleX = mutableFloatStateOf(0f)
    var touchScaleY = mutableFloatStateOf(0f)

    init {
        GlobalEventBus.subscribe<AndroidPixMapDraw> {
            if (!loaded)
                state.value = !state.value
        }
        GlobalEventBus.subscribe<Draw> {
            state.value = !state.value
            loaded = true
        }
        GlobalEventBus.subscribe<Draw>(priority = Int.MAX_VALUE) {
            if (mouseDown)
                waitFrame += 1
            else
                waitFrame = 0

            pendingTap?.let {
                waitTapFrame += 1
            }
        }
        GlobalEventBus.subscribe<Draw> {
            pendingMove?.let {
                mouseMoved(it.x, it.y)
                mouseDown = true
                pendingMove = null
                return@subscribe
            }
            pendingTap?.let {
                if (waitTapFrame == 0)
                    return@let
                mousePressed(it.x, it.y, 1)
                mouseReleased()
                pendingTap = null
            }
            pendingPress?.let {
                if (!mouseDown || waitFrame == 0)
                    return@let
                mousePressed(it.x, it.y, 1)
                pendingPress = null
            }
            pendingHold?.let {
                if (!mouseDown || waitFrame == 0)
                    return@let
                mousePressed(it.x, it.y, 2)
                mouseReleased()
                pendingHold = null
            }
        }
    }

    fun mouseMoved(x: Int, y: Int) {
        with(Client.client) {
            idleCycles = 0
            mouseX = x
            mouseY = y
        }
    }

    fun mousePressed(x: Int, y: Int, button: Int) {
        with(Client.client) {
            idleCycles = 0
            nextMouseClickX = x
            nextMouseClickY = y
            nextMouseClickTime = System.currentTimeMillis()
            nextMouseClickButton = button
            mouseButton = button
        }
    }

    fun mouseReleased() {
        with(Client.client) {
            idleCycles = 0
            mouseButton = 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        Thread {
            Client.context = applicationContext
            GameShell.context = AndroidPlatform()
            Client.vanillaMain()
        }.start()
        setContent {
            state.value
            GameSurface(Client.client.frame as? AndroidViewBox)
        }
    }

    override fun onResume() {
        super.onResume()
        Client.client?.let {
            Client.client.redrawFrame = true
            Client.client.redrawSidebar = true
            Client.client.redrawSideicons = true
            Client.client.redrawChatback = true
        }
    }

    @Composable
    fun GameSurface(viewBox: AndroidViewBox?) {
        viewBox ?: return
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onGloballyPositioned { layoutCoordinates ->
                containerSize.value = layoutCoordinates.size
                touchScaleX.value = containerSize.value.width.toFloat() / 765
                touchScaleY.value = containerSize.value.height.toFloat() / 503
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        pendingMove = Point(it.x.toInt(), it.y.toInt()).scaled()
                        pendingTap = Point(it.x.toInt(), it.y.toInt()).scaled() },
                    onLongPress = {
                        pendingMove = Point(it.x.toInt(), it.y.toInt()).scaled()
                        pendingHold = Point(it.x.toInt(), it.y.toInt()).scaled() }
                )
            }
            .pointerInteropFilter { change ->
                mouseMoved((change.x / touchScaleX.value).toInt(), (change.y / touchScaleY.value).toInt())
                false
            }.pointerInput(Unit) {
                detectDragGestures(onDragStart = {
                    pendingMove = Point(it.x.toInt(), it.y.toInt()).scaled()
                    pendingPress = Point(it.x.toInt(), it.y.toInt()).scaled()
                }, onDragCancel = {
                    mouseReleased()
                }, onDragEnd = {
                    mouseReleased()
                }) { change, dragAmount ->
                    pendingMove = Point(change.position.x.toInt(), change.position.y.toInt()).scaled()
                }
            }
        ) {
            Canvas(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                drawIntoCanvas { canvas ->
                    state.value

                    val destRect = android.graphics.Rect(
                        0, 0,
                        size.width.toInt(), size.height.toInt()
                    )

                    canvas.nativeCanvas.drawBitmap(
                        viewBox.bitmap,
                        null,
                        destRect,
                        paint
                    )
                }
            }
        }
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

