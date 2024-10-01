package com.meteor.android

import JinglePlayer
import SongPlayer
import android.content.Context
import android.graphics.Bitmap
import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.drm.DrmSessionManagerProvider
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.ByteArrayDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy
import com.meteor.android.MainActivity.Companion.displayText
import com.meteor.android.MainActivity.Companion.fps
import com.meteor.android.MainActivity.Companion.image
import com.meteor.android.MainActivity.Companion.pluginsLoaded
import com.meteor.android.MainActivity.Companion.recentDraws
import com.meteor.android.MainActivity.Companion.viewportImage
import com.meteor.android.ui.theme.MeteorAndroidTheme
import ext.kotlin.MutableStateExt.toggle
import jagex2.client.Client
import jagex2.graphics.Draw2D
import kotlinx.coroutines.GlobalScope
import meteor.Logger
import meteor.Main
import meteor.Main.forceRecomposition
import meteor.audio.SoundPlayer
import meteor.events.ClientInstance
import meteor.events.DrawFinished
import meteor.events.PlayJingle
import meteor.events.PlaySong
import meteor.events.PlaySound
import meteor.events.StopMusic
import meteor.events.ViewportDraw
import meteor.plugin.PluginManager
import meteor.ui.compose.components.Window
import meteor.ui.compose.components.Window.ViewBox
import org.rationalityfrontline.kevent.KEVENT
import org.rationalityfrontline.kevent.SubscriberThreadMode
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.ByteArrayInputStream
import java.io.File


class MainActivity : ComponentActivity() {
    companion object {
       //lateinit var synth: InterAppMidiSynthesizer
        var started = false
        var pluginsLoaded = false
        val displayText = mutableStateOf("Hello Android!")

        var receivedDraw = false
        var fps = mutableIntStateOf(0)
        var recentDraws = ArrayList<Long>()
        var image = mutableStateOf<ImageBitmap?>(null)
        var viewportImage = mutableStateOf<ImageBitmap?>(null)
        //var midiSystem : InterAppMidiSystem? = null

        private lateinit var midiManager: MidiManager
        private var midiDevice: MidiDevice? = null
        private val mainHandler = Handler(Looper.getMainLooper())
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
            Main.client = (Client.client as Any) as net.runelite.api.Client
        }
        KEVENT.subscribe<PlaySound> {
            mainHandler.post {
                SoundPlayer(it.data.sound, 0, applicationContext).play()
            }
        }
        KEVENT.subscribe<PlaySong> {
            mainHandler.post {
                SongPlayer(it.data.song, applicationContext)
            }
        }
        KEVENT.subscribe<StopMusic> {
            mainHandler.post {
                SongPlayer.release()
            }
        }
        KEVENT.subscribe<PlayJingle> {
            mainHandler.post {
                SongPlayer.release()
                JinglePlayer(it.data.crc, applicationContext)
            }
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
        super.onCreate(savedInstanceState)
        hideSystemUI()
        if (!started)  {
            Client.cacheDir = File(dataDir, "cache/")
            Client.main(emptyArray())
            started = true
        }
        Logger.logFile = File(dataDir, "log.txt")
        enableEdgeToEdge()
        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                ViewBox()
            }
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