package desktop

import androidx.compose.material.Text
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import common.ui.WindowImpl
import jagex3.client.Client
import jagex3.client.GameShell
import meteor.platform.vanilla.VanillaContext

object Main {
    @JvmStatic
    fun main(args: Array<String>) = application {
        GameShell.context = VanillaContext()
        Client.main(emptyArray<String>())
        Window(onCloseRequest = ::exitApplication) {
            WindowImpl.Window {
                Text("Hello World!")
            }
        }
    }
}