package meteor.context.platform.headless

import jagex2.client.GameShell
import jagex2.client.ViewBox
import meteor.context.PlatformContext

class HeadlessViewBox(width: Int, height: Int, ctx: PlatformContext) : ViewBox(ctx as GameShell, width, height) {
    init {
        this.shell = shell
    }
}