package meteor.platform.desktop

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import client.Client
import client.Client.RSA_MODULUS
import client.events.DrawFinished
import jagex2.client.Configuration
import jagex2.client.GameShell
import meteor.platform.common.Common.clientInstance
import meteor.platform.common.Common.eventbus
import meteor.platform.common.world.WorldsCommon.worlds
import sign.signlink
import java.net.InetAddress

/**
 * [gameImage]
 *  Compose format image where final images are drawn
 * [loadingImage]
 *  Game draws loading screens outside of draw() in a busy loop
 *  directly to parent containers graphics
 *  Hooking that would require code changes in deob, so we do it this way:
 *  While loading, the current image is drawn every 1ms
 *  Then the loading draw thread is discarded when draw() events fire off naturally
 */
object Game {
    val gameImage = mutableStateOf<ImageBitmap?>(null)
    val loadingImage = mutableStateOf<ImageBitmap?>(null)

    val loadingDrawThread = Thread {
        while (gameImage.value == null) {
            loadingImage.value = GameShell.image.toComposeImageBitmap()
            Thread.sleep(1)
        }
    }

    init {
        RSA_MODULUS = worlds.first.modulus
        eventbus.subscribe<DrawFinished> {
            gameImage.value = GameShell.image.toComposeImageBitmap()
        }
    }

    fun init() {
        clientInstance = Client() as net.runelite.api.Client
        clientInstance.setCallbacks(Hooks)
        Client.nodeId = 10
        Client.portOffset = 0
        Client.setHighMemory()
        Client.members = false
        signlink.startpriv(InetAddress.getByName("localhost"))
        Configuration.INTERCEPT_GRAPHICS = true
        clientInstance.`initApplication$api`(789, 532)
        loadingDrawThread.start()
    }
}