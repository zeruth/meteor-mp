package meteor.audio

import android.content.Context
import android.media.AudioTrack
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import java.io.File
import java.io.FileOutputStream

class SoundPlayer(private val audioData: ByteArray, private val delay: Int, private val context: Context) {
    var tmpID = -1

    fun play() {
        tmpID++
        val tempWav = File.createTempFile("sound$tmpID", "wav")
        tempWav.deleteOnExit()
        val fos = FileOutputStream(tempWav)
        fos.write(audioData)
        fos.close()

        val mediaItem = MediaItem.fromUri(tempWav.toURI().toString())

        startExoPlayer(mediaItem)
    }

    fun startExoPlayer(media: MediaItem) {
        val player = ExoPlayer.Builder(context).build()

        player.setMediaItem(media)
        player.prepare()
        player.play()
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    player.release()
                }
            }
        })
    }
}
