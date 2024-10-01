import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.meteor.android.R

class JinglePlayer(crc: Long, private val context: Context) {
    companion object {
        var playing = false
        var player: ExoPlayer? = null

        fun release() {
            player?.release()
            player = null
        }
    }

    init {
        val resource = when (crc) {
            2051451258L -> R.raw.death2
            else -> {
                throw Exception("Missing Jingle: $crc")
            }
        }
        playAudioFromRaw(resource)
    }

    fun playAudioFromRaw(resourceId: Int) {
        if (player != null) {
            player?.release()
            player = null
        }

        player = ExoPlayer.Builder(context).build()

        val uri = Uri.parse("rawresource://" + context.packageName + "/" + resourceId)
        val mediaItem = MediaItem.fromUri(uri)

        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
        playing = true
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    playing = false
                    player?.release()
                    SongPlayer.lastSong?.let {
                        SongPlayer(it, context)
                    }
                }
            }
        })
    }


}