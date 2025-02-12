package meteor

import JinglePlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import client.events.MidiJinglePlay
import client.events.MidiPlay
import client.events.MidiStop
import client.events.WavePlay
import client.events.WaveReplay
import com.meteor.nat.awt.R
import ext.android.ComponentActivityExt.setupActivity
import meteor.audio.SongPlayer
import meteor.audio.SoundPlayer
import meteor.common.Common
import meteor.common.Common.clientInstance
import meteor.common.plugin.PluginManager
import meteor.input.KeyboardController.keyboardController
import meteor.ui.Window.MeteorViewBox
import org.rationalityfrontline.kevent.KEVENT
import java.io.File


class MainActivity : ComponentActivity() {

    private val mainHandler = Handler(Looper.getMainLooper())
    var preventReplay = true

    companion object {
        fun onlyPlayJingles(): Boolean {
            return false
        }

        var songPlayer: SongPlayer? = null
        var jinglePlayer: JinglePlayer? = null
        var lastSong: String? = null
        var muteLoginMusic = false
    }

    init {
        /**
         * Subscribe to sounds here so we have access to context
         */
        KEVENT.subscribe<WavePlay> {
            Thread {
                preventReplay = true
                val bytes = it.data.soundStream.use {
                    val arr = ArrayList<Byte>()
                    while (it.available() > 0)
                        arr.add(it.read().toByte())
                    arr.toByteArray()
                }
                mainHandler.post {
                    Log.d("AUDIO", "PlaySound")
                    SoundPlayer.lastWaveBytes = bytes
                    SoundPlayer(bytes, 0, applicationContext).play()
                }
                preventReplay = false
            }.start()
        }
        KEVENT.subscribe<WaveReplay> {
            Thread {
                if (preventReplay) {
                    while (preventReplay) {
                        Thread.sleep(1)
                    }
                }
                mainHandler.post {
                    Log.d("AUDIO", "PlaySound")
                    SoundPlayer(SoundPlayer.lastWaveBytes, 0, applicationContext).play()
                }
            }.start()
        }
        KEVENT.subscribe<MidiPlay> {
            mainHandler.post {
                var prevSong = lastSong
                lastSong = it.data.name
                if (it.data.name == "scape_main") {
                    if (muteLoginMusic || (clientInstance.ingame && onlyPlayJingles()) || prevSong == "scape_main") {
                        return@post
                    }
                }
                if (clientInstance.ingame) {
                    if (!onlyPlayJingles()) {
                        songPlayer?.release()
                        songPlayer = SongPlayer(it.data.name, applicationContext)
                    }
                } else {
                    songPlayer?.release()
                    songPlayer = SongPlayer(it.data.name, applicationContext)
                }
            }
        }
        KEVENT.subscribe<MidiStop> {
            mainHandler.post {
                songPlayer?.release()
            }
        }
        KEVENT.subscribe<MidiJinglePlay> {
            mainHandler.post {
                try {
                    songPlayer?.release()
                    jinglePlayer?.release()
                    jinglePlayer = JinglePlayer(it.data, applicationContext)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadMeteor()
        setContent {
            keyboardController = LocalSoftwareKeyboardController.current!!
            val focusRequester = remember { FocusRequester() }
            Box(modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)) {
                MeteorViewBox()
            }
        }
    }

    private fun loadMeteor() {
        setupActivity()
        setupMeteor()
        Game.start()
        PluginManager.start()
    }

    private fun setupMeteor() {
        Common.isAndroid = true
        Logger.logFile = File(dataDir, "log.txt")
        meteor.common.Configuration.init(applicationContext)
    }
}

