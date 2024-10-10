package meteor.plugin.sound

import JinglePlayer
import com.meteor.android.MainActivity
import meteor.Main
import meteor.Main.client
import meteor.events.LoggedInChanged
import meteor.events.PlaySong
import meteor.events.StopMusic
import meteor.events.client.ConfigChanged
import meteor.plugin.Plugin
import meteor.ui.compose.components.GamePanel
import meteor.ui.config.AspectMode

class SoundPlugin : Plugin("Sound", cantDisable = true) {
    val config = configuration<SoundConfig>()
    override fun onStart() {
        client.setOnlyPlayJingles(config.onlyPlayJingles.get<Boolean>())
        updateSong()
    }

    fun updateSong() {
        if (client.isLoggedIn) {
            if (client.onlyPlayJingles()) {
                client.callbacks.post(StopMusic)
            } else {
                MainActivity.lastSong?.let {
                    client.callbacks.post(PlaySong(it))
                }
            }
        }
    }

    override fun onStop() {
        client.setOnlyPlayJingles(false)
        updateSong()
    }

    override fun onConfigChanged(it: ConfigChanged) {
        if (it.affects(config)) {
            client.setOnlyPlayJingles(config.onlyPlayJingles.get<Boolean>())
            updateSong()
        }
    }

    override fun onLoggedInChanged(it: LoggedInChanged) {
        updateSong()
    }
}