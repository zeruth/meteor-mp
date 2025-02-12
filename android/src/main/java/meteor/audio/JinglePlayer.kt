import android.content.Context
import android.net.Uri
import client.events.MidiJinglePlay
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.meteor.android.R
import meteor.MainActivity
import meteor.audio.SongPlayer

class JinglePlayer(event: MidiJinglePlay, private val context: Context) {
    var player = ExoPlayer.Builder(context).build()
    companion object {
        var playing = false
    }
    init {
        val resource = when (event.crc) {
            2692167083L -> R.raw.advance_agility
            3340661877L -> R.raw.advance_attack
            890696254L -> R.raw.advance_attack2
            3084927869L -> R.raw.advance_cooking
            2258195486L -> R.raw.advance_cooking2
            1240130821L -> R.raw.advance_crafting
            3042778074L -> R.raw.advance_crafting2 //EOF EXC
            3866177329L -> R.raw.advance_defense
            1626507290L -> R.raw.advance_defense2
            3659074766L -> R.raw.advance_firemarking
            2011825278L -> R.raw.advance_firemarking2
            171721463L -> R.raw.advance_fishing
            1618703117L -> R.raw.advance_fishing2
            3464207021L -> R.raw.advance_fletching
            2134192119L -> R.raw.advance_fletching2
            2987367138L -> R.raw.advance_herblaw
            3863450869L -> R.raw.advance_herblaw2 //EOF EXC
            399916944L -> R.raw.advance_hitpoints
            2084537792L -> R.raw.advance_hitpoints2
            1813408522L -> R.raw.advance_magic
            1213830499L -> R.raw.advance_magic2
            2033693426L -> R.raw.advance_mining
            2777815745L -> R.raw.advance_mining2
            1774561144L -> R.raw.advance_prayer
            1758869464L -> R.raw.advance_prayer2
            146851785L -> R.raw.advance_ranged
            3118754474L -> R.raw.advance_ranged2
            622369582L -> R.raw.advance_runecraft
            3128276047L -> R.raw.advance_runecraft2
            1782471581L -> R.raw.advance_smithing
            2036932628L -> R.raw.advance_smithing2
            3871031931L -> R.raw.advance_strength
            465621234L -> R.raw.advance_strength2
            4143630970L -> R.raw.advance_thieving
            2034336695L -> R.raw.advance_thieving2
            1511562920L -> R.raw.advance_woodcutting
            1835379897L -> R.raw.advance_woodcutting2
            919031448L -> R.raw.death
            69335160L -> R.raw.death2
            2289242941L -> R.raw.dice_lose
            717181009L -> R.raw.dice_win
            3102502673L -> R.raw.duel_start
            872493916L -> R.raw.duel_win2
            297774013L -> R.raw.quest_complete1
            3353484147L -> R.raw.quest_complete2
            3684956700L -> R.raw.quest_complete3
            281721552L -> R.raw.sailing_journey
            3492187825L -> R.raw.treasure_hunt_win
            else -> {
                throw Exception("Missing Jingle: ${event.crc}")
            }
        }
        playAudioFromRaw(resource)
        event.consume()
    }

    fun release() {
        player.release()
    }

    fun playAudioFromRaw(resourceId: Int) {
        if (player.isPlaying)
            player.release()
        player = ExoPlayer.Builder(context).build()

        val uri = Uri.parse("rawresource://" + context.packageName + "/" + resourceId)
        val mediaItem = MediaItem.fromUri(uri)

        player.volume = 1f
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        playing = true
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    playing = false
                    player.release()
                    if (!MainActivity.onlyPlayJingles())
                        MainActivity.lastSong?.let {
                            MainActivity.songPlayer?.release()
                            MainActivity.songPlayer = SongPlayer(it, context)
                        }
                }
            }
        })
    }


}