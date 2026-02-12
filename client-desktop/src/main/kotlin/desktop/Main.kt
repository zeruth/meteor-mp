package desktop

import androidx.compose.material.Text
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import jagex2.client.Client
import jagex2.client.GameShell
import common.ui.WindowImpl
import meteor.platform.vanilla.VanillaContext

object Main {
    @JvmStatic
    fun main(args: Array<String>) = application {
        GameShell.context = VanillaContext()
        Client.vanillaMain()
        Window(onCloseRequest = ::exitApplication) {
            WindowImpl.Window {
                Text("Hello World!")
            }
        }
    }
}