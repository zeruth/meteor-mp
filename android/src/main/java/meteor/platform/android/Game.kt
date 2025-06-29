package meteor.platform.android

import android.graphics.Bitmap
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import client.Client
import client.Client.RSA_MODULUS
import client.events.DrawFinished
import ext.awt.BufferedImageExt.getPixels
import jagex2.client.Configuration
import meteor.platform.common.Common.clientInstance
import meteor.platform.common.Common.eventbus
import sign.signlink
import java.lang.Thread.sleep
import java.net.InetAddress

object Game {
    var receivedDraw = false
    var fps = mutableIntStateOf(0)
    var recentDraws = ArrayList<Long>()
    var image = mutableStateOf<ImageBitmap?>(null)

    //Draws when game reports a frame
    init {
        eventbus.subscribe<DrawFinished> {
            receivedDraw = true
            updateGameImage()
        }
    }

    fun start() {
        Client.isAndroid = true
        clientInstance = Client() as net.runelite.api.Client
        clientInstance.callbacks = Hooks
        Client.nodeId = 10
        Client.portOffset = 0
        Client.setHighMemory()
        Client.members = false
        Thread {
            signlink.startpriv(InetAddress.getByName("localhost"))
        }.start()
        sleep(500)
        Configuration.INTERCEPT_GRAPHICS = true
        clientInstance.`initApplication$api`(789, 532)

        //Draws while loading (stuck in while loop during cache loading with no draw reporting)
        Thread {
            while (!receivedDraw) {
                updateGameImage()
            }
        }.start()
    }

    fun updateGameImage() {
        try {
            image.value = Bitmap.createBitmap(Client.image.getPixels(), Client.image.width, Client.image.height, Bitmap.Config.RGB_565).asImageBitmap()
            recentDraws += System.currentTimeMillis()
            val expiredTimes = ArrayList<Long>()
            for (renderTime in recentDraws) {
                if (renderTime < (System.currentTimeMillis() - 1000))
                    expiredTimes += renderTime
            }
            for (expiredTime in expiredTimes)
                recentDraws.remove(expiredTime)
            fps.intValue = recentDraws.size

        } catch (_: Exception) {}
    }
}

