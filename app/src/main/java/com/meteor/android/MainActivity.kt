package com.meteor.android

import android.content.Context
import android.graphics.Bitmap
import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.os.Bundle
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
import meteor.Logger
import meteor.Main
import meteor.Main.forceRecomposition
import meteor.audio.SoundPlayer
import meteor.events.ClientInstance
import meteor.events.DrawFinished
import meteor.events.PlaySong
import meteor.events.PlaySound
import meteor.events.StopMusic
import meteor.events.ViewportDraw
import meteor.plugin.PluginManager
import meteor.ui.compose.components.Window
import meteor.ui.compose.components.Window.ViewBox
import org.rationalityfrontline.kevent.KEVENT
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
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
            println("play sound")
            SoundPlayer(it.data.sound, 0).play()
        }
/*        KEVENT.subscribe<PlaySong> { MidiPlayer.playSong(false, baseContext) }
        KEVENT.subscribe<StopMusic> { MidiPlayer.stop() }*/
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

/*    fun playSong() {
        midiDevice.info.ports.get(0).se
    }*/

/*    private fun openMidiDevice(deviceIndex: Int) {
        val devices = midiManager.devices
        if (deviceIndex >= devices.size) {
            Log.e("MidiPlayer", "Invalid device index")
            return
        }

        val deviceInfo = devices[deviceIndex]
        midiManager.openDevice(deviceInfo, object : MidiManager.OnDeviceOpenedListener {
            override fun onDeviceOpened(device: MidiDevice) {
                midiDevice = device
                Log.d("MidiPlayer", "MIDI device opened: ${deviceInfo.properties.getString(
                    MidiDeviceInfo.PROPERTY_NAME)}")
                // You can now use the MIDI device to send/receive MIDI messages
            }
        }, null)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // midiManager = getSystemService(Context.MIDI_SERVICE) as MidiManager
        //openMidiDevice(0)
        // Initialize FluidSynth
        //fluidSynth = FluidSynth()
/*
        // Load SoundFont (replace with your SoundFont file path)
        val soundFontPath = "path/to/your/soundfont.sf2"
        fluidSynth.loadSoundFont(soundFontPath)*/
        //midiSystem = InterAppMidiSystem(baseContext)
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