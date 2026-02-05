package meteor.context.platform.headless

import jagex2.client.Client
import jagex2.client.ViewBox

class HeadlessViewBox(width: Int, height: Int) : ViewBox(Client.client, width, height) {
    init {
        this.shell = shell
    }
}