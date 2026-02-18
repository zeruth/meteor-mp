package android

import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import common.ui.WindowImpl
import jagex3.client.Client
import jagex3.client.Client.orbitCameraPitch
import jagex3.client.Client.orbitCameraYaw
import jagex3.client.GameShell
import jagex3.client.input.mouse.ClientMouseListener
import jagex3.util.MonotonicTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import meteor.events.Draw
import meteor.platform.android.AndroidContext
import util.GlobalEventBus
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {

    companion object {
        private val clientDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        private val clientScope = CoroutineScope(clientDispatcher + SupervisorJob())

        fun Client.invoke(task: suspend Client.() -> Unit) {
            clientScope.launch {
                task(this@invoke)
            }
        }
    }
    val paint = Paint()

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
    var draws = ArrayList<Long>()
    var lastCheck = System.currentTimeMillis()

    init {
        GlobalEventBus.subscribe<Draw> {
            state.value = !state.value
            draws += System.currentTimeMillis()

            if (System.currentTimeMillis() - lastCheck > 1000) {
                lastCheck = System.currentTimeMillis()
                println("FPS: ${draws.size}")
                draws.clear()
            }
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        clientScope.launch {
/*            //Client.context = applicationContext
*//*            GameShell.context = AndroidPlatform()
            Client.vanillaMain()*/
            GameShell.context = AndroidContext(this@MainActivity)
            Client.vanillaMain()
        }
        setContent {
            state.value
            WindowImpl.Window {
                GameSurface()
            }
        }
    }

    override fun onResume() {
        super.onResume()
/*        Client.client?.let {
            Client.client.redrawFrame = true
            Client.client.redrawSidebar = true
            Client.client.redrawSideicons = true
            Client.client.redrawChatback = true
        }*/
    }

    suspend fun PointerInputScope.detectOneFingerDrag(
        onDrag: (offset: Offset) -> Unit
    ) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                val pointers = event.changes

                if (pointers.size == 1) {
                    pendingPress = null
                    pendingTap = null
                    val delta = pointers[0].positionChange()

                    if (delta != Offset.Companion.Zero) {
                        onDrag(delta)
                        pointers.forEach { it.consume() }
                    }
                }
            }
        }
    }

    var twoFingers = false

    suspend fun PointerInputScope.detectTwoFingerDrag(
        onDrag: (offset: Offset) -> Unit
    ) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                val pointers = event.changes

                if (pointers.size == 2) {
                    twoFingers = true
                    val delta0 = pointers[0].positionChange()
                    val delta1 = pointers[1].positionChange()
                    val averageDelta = (delta0 + delta1) / 2f

                    if (averageDelta != Offset.Companion.Zero) {
                        onDrag(averageDelta)
                        pointers.forEach { it.consume() }
                    }
                } else
                    twoFingers = false
            }
        }
    }

    @Composable
    fun GameSurface() {
        Box(
            modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onGloballyPositioned { layoutCoordinates ->
                containerSize.value = layoutCoordinates.size
                touchScaleX.floatValue = containerSize.value.width.toFloat() / 765
                touchScaleY.floatValue = containerSize.value.height.toFloat() / 503
            }
            .pointerInteropFilter { change ->
                mouseMoved((change.x / touchScaleX.value).toInt(), (change.y / touchScaleY.value).toInt())
                false
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        pendingMove = Point(it.x.toInt(), it.y.toInt()).scaled()
                        pendingTap = Point(it.x.toInt(), it.y.toInt()).scaled()
                    },
                    onLongPress = {
                        pendingMove = Point(it.x.toInt(), it.y.toInt()).scaled()
                        pendingHold = Point(it.x.toInt(), it.y.toInt()).scaled()
                    }
                )
            }
                .pointerInput(Unit) {
                    detectTwoFingerDrag { delta ->
                        (GameShell.shell as Client).invoke {
                            orbitCameraYaw -= delta.x.toInt()
                            orbitCameraPitch += delta.y.toInt()
                        }
                    }
                }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            if (twoFingers) break

                            val down = awaitFirstDown()
                            pendingMove = pendingPress

                            val pointerId = down.id
                            var lastPoint: Point? = null
                            while (true) {
                                if (twoFingers) break

                                val event = awaitPointerEvent()
                                val dragChange = event.changes.firstOrNull { it.id == pointerId } ?: break
                                lastPoint = Point(dragChange.position.x.toInt(), dragChange.position.y.toInt()).scaled()
                                pendingMove = lastPoint

                                if (dragChange.changedToUp()) {
                                    mouseReleased()
                                    break
                                }
                                dragChange.consume()
                            }
                            pendingTap = lastPoint
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                drawIntoCanvas { canvas ->
                    state.value

                    val destRect = Rect(
                        0, 0,
                        size.width.toInt(), size.height.toInt()
                    )

                    canvas.nativeCanvas.drawBitmap(
                        (GameShell.context as AndroidContext).bitmap,
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