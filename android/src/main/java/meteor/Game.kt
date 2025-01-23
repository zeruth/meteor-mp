package meteor

import android.graphics.Bitmap
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import client.Configuration
import client.client
import client.events.DrawFinished
import ext.awt.BufferedImageExt.getPixels
import meteor.MainActivity.Companion.clientInstance
import meteor.common.Common.eventbus
import sign.signlink
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
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
        clientInstance = client()
        client.isAndroid = true
        client.nodeId = 10
        client.portOffset = 0
        client.setHighMemory()
        client.members = false
        Thread {
            signlink.startpriv(InetAddress.getByName("localhost"))
        }.start()
        sleep(500)
        Configuration.INTERCEPT_GRAPHICS = true
        clientInstance.initApplication(789, 532)

        //Draws while loading (stuck in while loop during cache loading with no draw reporting)
        Thread {
            while (!receivedDraw) {
                updateGameImage()
            }
        }.start()
    }

    fun updateGameImage() {
        try {
            image.value = Bitmap.createBitmap(client.image.getPixels(), client.image.width, client.image.height, Bitmap.Config.RGB_565).asImageBitmap()
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

