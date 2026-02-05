package desktop

import jagex2.client.Client
import jagex2.client.GameShell
import meteor.context.platform.vanilla.VanillaPlatform

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        GameShell.context = VanillaPlatform()
        Client.vanillaMain()
    }
}