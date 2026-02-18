package desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import common.ui.WindowImpl
import desktop.GameComposables.Game
import desktop.GameComposables.GameInputLayer
import jagex3.client.Client
import jagex3.client.GameShell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import meteor.events.Draw
import meteor.platform.desktop.DesktopContext
import util.GlobalEventBus
import java.util.concurrent.Executors

object Main {
    var draws = ArrayList<Long>()
    var lastCheck = System.currentTimeMillis()

    init {
        System.setProperty("compose.interop.blending", "true")
        GameShell.context = DesktopContext()

        GlobalEventBus.subscribe<Draw> {
            state.value = !state.value
            draws += System.currentTimeMillis()

            if (System.currentTimeMillis() - lastCheck > 1000) {
                lastCheck = System.currentTimeMillis()
                println("FPS: ${draws.size}")
                draws.clear()
            }
        }
    }

    private val state = mutableStateOf(false)
    private val clientDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val clientScope = CoroutineScope(clientDispatcher + SupervisorJob())
    var touchScaleX = mutableFloatStateOf(0f)
    var touchScaleY = mutableFloatStateOf(0f)
    var panel = DesktopJPanel()
    val focusRequester = FocusRequester()

    @OptIn(ExperimentalComposeUiApi::class)
    @JvmStatic
    fun main(args: Array<String>) = application {

        clientScope.launch {
            Client.vanillaMain()
        }

        Window(onCloseRequest = ::exitApplication) {
            WindowImpl.Window {
                Box(modifier = Modifier.fillMaxSize().background(Color.Blue)) {
                    Game()
                    GameInputLayer()
                    //Text("Hello World ${state.value}", Modifier.align(Alignment.Center))
                }
            }
        }
    }

    fun context() = GameShell.context as DesktopContext

    fun client() = GameShell.shell as Client

    fun Client.invoke(task: suspend Client.() -> Unit) {
        clientScope.launch {
            task(this@invoke)
        }
    }

    fun invoke(task: suspend Client.() -> Unit) {
        client().invoke(task)
    }
}