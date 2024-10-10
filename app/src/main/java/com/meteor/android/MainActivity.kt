package com.meteor.android

import JinglePlayer
import meteor.audio.SongPlayer
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.media.midi.MidiDevice
import android.media.midi.MidiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnHoverListener
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import com.meteor.android.MainActivity.Companion.displayText
import com.meteor.android.MainActivity.Companion.fps
import com.meteor.android.MainActivity.Companion.image
import com.meteor.android.MainActivity.Companion.pluginsLoaded
import com.meteor.android.MainActivity.Companion.recentDraws
import com.meteor.android.ui.theme.MeteorAndroidTheme
import ext.kotlin.MutableStateExt.toggle
import jagex2.client.Client
import jagex2.client.GameShell
import meteor.Logger
import meteor.Main
import meteor.Main.client
import meteor.Main.forceRecomposition
import meteor.audio.SoundPlayer
import meteor.events.ChangeMusicVolume
import meteor.events.ChangeSoundVolume
import meteor.events.ClientInstance
import meteor.events.DrawFinished
import meteor.events.InterfaceChanged
import meteor.events.PlayJingle
import meteor.events.PlaySong
import meteor.events.PlaySound
import meteor.events.StopMusic
import meteor.events.ViewportDraw
import meteor.events.client.KeyboardButtonEvent
import meteor.plugin.PluginManager
import meteor.ui.compose.components.GamePanel.touchScaleX
import meteor.ui.compose.components.GamePanel.touchScaleY
import meteor.ui.compose.components.Window.ViewBox
import meteor.ui.compose.overlay.ViewportOverlayRoot.lastTextLength
import net.runelite.api.VolumeSetting
import org.rationalityfrontline.kevent.KEVENT
import java.awt.Point
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.File


class MainActivity : ComponentActivity() {
    companion object {
        var muteLoginMusic = false

        //lateinit var synth: InterAppMidiSynthesizer
        var started = false
        var pluginsLoaded = false
        val displayText = mutableStateOf("Hello Android!")

        var receivedDraw = false
        var fps = mutableIntStateOf(0)
        var recentDraws = ArrayList<Long>()
        var image = mutableStateOf<ImageBitmap?>(null)
        var viewportImage = mutableStateOf<ImageBitmap?>(null)
        var currentText = mutableStateOf("")
        var showTextInput = mutableStateOf(false)
        //var midiSystem : InterAppMidiSystem? = null

        var lastSong: String? = null
        private lateinit var midiManager: MidiManager
        private var midiDevice: MidiDevice? = null
        private val mainHandler = Handler(Looper.getMainLooper())
        lateinit var keyboardController: SoftwareKeyboardController
        lateinit var focusRequester: FocusRequester
        var musicVolume = VolumeSetting.FOUR
        var soundVolume = VolumeSetting.FOUR
        var songPlayer: SongPlayer? = null
        var jinglePlayer: JinglePlayer? = null

        fun handleKeyEvent(event: KeyEvent): Boolean {
            println("received key event")
            var asciiKey = event.unicodeChar
            if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
                lastTextLength = 0
                showTextInput.value = false
                keyboardController.hide()
                currentText.value = ""
            }

            if (event.keyCode == KeyEvent.KEYCODE_DEL) {
                asciiKey = 8 // ASCII for backspace
            }

            if (event.action == KeyEvent.ACTION_DOWN) {
                GameShell.keyPressed(asciiKey)
            } else if (event.action == KeyEvent.ACTION_UP) {
                GameShell.keyReleased(asciiKey)
                GameShell.keyTyped(asciiKey)
            }

            return false // Consume the event
        }
    }

    init {
        Client.hooks = Hooks()
        KEVENT.subscribe<DrawFinished> {
            receivedDraw = true
            updateGameImage(true)
        }
        KEVENT.subscribe<ViewportDraw> {
            receivedDraw = true
            val viewport = Main.client.areaViewport.image as BufferedImage
            viewportImage.value = Bitmap.createBitmap(getPixelArray(viewport), viewport.width, viewport.height, Bitmap.Config.RGB_565).asImageBitmap()
            updateGameImage(false)
        }
        KEVENT.subscribe<ClientInstance> {
            client = (Client.client as Any) as net.runelite.api.Client
        }
        KEVENT.subscribe<PlaySound> {
            mainHandler.post {
                SoundPlayer(it.data.sound, 0, applicationContext).play()
            }
        }
        KEVENT.subscribe<ChangeMusicVolume> {
            musicVolume = it.data.volumeSetting
            mainHandler.post {
                songPlayer?.player?.volume = it.data.volumeSetting.volume
                jinglePlayer?.player?.volume = it.data.volumeSetting.volume
            }
        }
        KEVENT.subscribe<ChangeSoundVolume> {
            mainHandler.post {
                soundVolume = it.data.volumeSetting
            }
        }
        KEVENT.subscribe<PlaySong> {
            mainHandler.post {
                var prevSong = lastSong
                lastSong = it.data.song
                if (it.data.song == "scape_main") {
                    if (muteLoginMusic || (client.isLoggedIn && client.onlyPlayJingles()) || prevSong == "scape_main") {
                        return@post
                    }
                }
                if (client.isLoggedIn) {
                    if (!client.onlyPlayJingles()) {
                        songPlayer?.release()
                        songPlayer = SongPlayer(it.data.song, applicationContext)
                    }
                } else {
                    songPlayer?.release()
                    songPlayer = SongPlayer(it.data.song, applicationContext)
                }
            }
        }
        KEVENT.subscribe<StopMusic> {
            mainHandler.post {
                songPlayer?.release()
            }
        }
        KEVENT.subscribe<PlayJingle> {
            mainHandler.post {
                songPlayer?.release()
                jinglePlayer?.release()
                jinglePlayer = JinglePlayer(it.data.crc, applicationContext)
            }
        }
        KEVENT.subscribe<KeyboardButtonEvent> {
            showTextInput.value = true
            showSoftKeyboard()
        }
        KEVENT.subscribe<InterfaceChanged> {
            Main.interfaceOpen.value = it.data.interfaceID != -1
        }
        Thread {
            while (!receivedDraw) {
                updateGameImage(false)
            }
        }.start()
    }


    private fun hideSystemUI() {
        // Hide the status bar and navigation bar
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
        //findViewById<View>(android.R.id.content).setOnHoverListener(hoverListener())
        if (!started)  {
            Client.cacheDir = File(dataDir, "cache/")
            Client.main(emptyArray())
            started = true
        }
        Logger.logFile = File(dataDir, "log.txt")
        enableEdgeToEdge()
        setContent {
            //LocalView.current.setOnHoverListener(hoverListener())
            keyboardController = LocalSoftwareKeyboardController.current!!
            focusRequester = remember { FocusRequester() }
            Box(modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)) {
                ViewBox()
            }
        }
    }

    fun hoverListener(): OnHoverListener {
        return OnHoverListener { _: View, event: MotionEvent ->
            val point = createScaledPoint(event.x, event.y)
            GameShell.mouseMoved(point.x, point.y)
            return@OnHoverListener true
        }
    }

    private fun createScaledPoint(x: Float, y: Float): Point {
        val scaledX = x / touchScaleX
        val scaledY = y / touchScaleY

        return Point(scaledX.toInt(), scaledY.toInt());
    }

    fun showSoftKeyboard(inputType: Int = InputType.TYPE_CLASS_TEXT) {
        runOnUiThread {
            focusRequester.requestFocus()
            keyboardController.show()
/*            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(findViewById(android.R.id.content), 0)*/

            //findViewById<View>(android.R.id.content)?.requestFocus()
        }
    }
}

fun updateGameImage(finalDraw: Boolean) {
    try {
        if (!pluginsLoaded) {
            PluginManager.startPlugins()
            pluginsLoaded = true
        }

        image.value = Bitmap.createBitmap(getPixelArray(Client.frame), Client.frame.width, Client.frame.height, Bitmap.Config.RGB_565).asImageBitmap()

        forceRecomposition.toggle()
        if (finalDraw) {
            recentDraws += System.currentTimeMillis()
            val expiredTimes = ArrayList<Long>()
            for (renderTime in recentDraws) {
                if (renderTime < (System.currentTimeMillis() - 1000))
                    expiredTimes += renderTime
            }
            for (expiredTime in expiredTimes)
                recentDraws.remove(expiredTime)
            fps.intValue = recentDraws.size
        }

    } catch (_: Exception) {}
}

fun getPixelArray(image: BufferedImage): IntArray {
    return (image.raster.dataBuffer as DataBufferInt).data
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = displayText.value,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MeteorAndroidTheme {
        Greeting("Android")
    }
}