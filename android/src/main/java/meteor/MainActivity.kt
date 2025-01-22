package meteor

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import client.Configuration
import client.client
import client.events.DrawFinished
import meteor.MainActivity.Companion.fps
import meteor.MainActivity.Companion.image
import meteor.MainActivity.Companion.recentDraws
import ext.awt.BufferedImageExt.toComposeImageBitmap
import meteor.common.Common
import meteor.common.Common.eventbus
import meteor.common.plugin.PluginManager
import meteor.ui.Window.ViewBox
import sign.signlink
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.File
import java.lang.Thread.sleep
import java.net.InetAddress
import jagex2.client.GameShell.image as rs2Image


class MainActivity : ComponentActivity() {
    companion object {
        lateinit var clientInstance: client

        val gameImage = mutableStateOf<ImageBitmap?>(null)
        val loadingImage = mutableStateOf<ImageBitmap?>(null)

        val loadingDrawThread = Thread {
            while (gameImage.value == null) {
                loadingImage.value = rs2Image.toComposeImageBitmap()
                sleep(1)
            }
        }


        var receivedDraw = false
        var fps = mutableIntStateOf(0)
        var recentDraws = ArrayList<Long>()
        var image = mutableStateOf<ImageBitmap?>(null)
        var currentText = mutableStateOf("")
        var showTextInput = mutableStateOf(false)

        lateinit var keyboardController: SoftwareKeyboardController
        lateinit var focusRequester: FocusRequester

        fun handleKeyEvent(event: KeyEvent): Boolean {
            println("received key event")
            var asciiKey = event.unicodeChar
            if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                showTextInput.value = false
                keyboardController.hide()
                currentText.value = ""
            }

            if (event.keyCode == KeyEvent.KEYCODE_DEL) {
                asciiKey = 8
            }

            if (event.action == KeyEvent.ACTION_DOWN) {
                clientInstance.keyPressed(asciiKey, -1)
            } else if (event.action == KeyEvent.ACTION_UP) {
                clientInstance.keyReleased(asciiKey)
                clientInstance.keyTyped(asciiKey)
            }

            return false
        }
    }

    fun startGame() {
        eventbus.subscribe<DrawFinished> {
            receivedDraw = true
            updateGameImage()
        }
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
        loadingDrawThread.start()

        Thread {
            while (!receivedDraw) {
                updateGameImage()
            }
        }.start()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onBackPressedDispatcher.addCallback {
            showTextInput.value = false
        }
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        hideSystemUI()
        Common.isAndroid = true
        meteor.common.Configuration.init(applicationContext)
        Logger.logFile = File(dataDir, "log.txt")
        startGame()
        PluginManager.startPlugins()
        enableEdgeToEdge()
        setContent {
            keyboardController = LocalSoftwareKeyboardController.current!!
            focusRequester = remember { FocusRequester() }
            Box(modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)) {
                ViewBox()
            }
        }
    }
}

fun updateGameImage() {
    try {
        image.value = Bitmap.createBitmap(getPixelArray(client.image), client.image.width, client.image.height, Bitmap.Config.RGB_565).asImageBitmap()
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

fun getPixelArray(image: BufferedImage): IntArray {
    return (image.raster.dataBuffer as DataBufferInt).data
}