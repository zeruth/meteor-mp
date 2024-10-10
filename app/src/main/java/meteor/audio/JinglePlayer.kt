import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.meteor.android.MainActivity
import com.meteor.android.R
import meteor.Main
import meteor.audio.SongPlayer
import net.runelite.api.VolumeSetting

class JinglePlayer(crc: Long, private val context: Context) {
    var player = ExoPlayer.Builder(context).build()
    companion object {
        var playing = false
    }
    init {
        val resource = when (crc) {
            1628002272L -> R.raw.advance_agility
            2772076602L -> R.raw.advance_attack
            3388583544L -> R.raw.advance_attack2
            1584589117L -> R.raw.advance_cooking
            3269587629L -> R.raw.advance_cooking2
            1877783343L -> R.raw.advance_crafting
            992402271L -> R.raw.advance_crafting2 //EOF EXC
            3690604311L -> R.raw.advance_defense
            4094317177L -> R.raw.advance_defense2
            1037120741L -> R.raw.advance_firemarking
            2765809608L -> R.raw.advance_firemarking2
            3151726566L -> R.raw.advance_fishing
            2829521800L -> R.raw.advance_fishing2
            782853764L -> R.raw.advance_fletching
            3636562659L -> R.raw.advance_fletching2
            1659964614L -> R.raw.advance_herblaw
            1458232409L -> R.raw.advance_herblaw2 //EOF EXC
            1050625824L -> R.raw.advance_hitpoints
            3242409699L -> R.raw.advance_hitpoints2
            3722870568L -> R.raw.advance_magic
            1423943549L -> R.raw.advance_magic2
            192394263L -> R.raw.advance_mining
            2881932690L -> R.raw.advance_mining2
            3191218446L -> R.raw.advance_prayer
            2360979466L -> R.raw.advance_prayer2
            682352735L -> R.raw.advance_ranged
            544300157L -> R.raw.advance_ranged2
            1399209304L -> R.raw.advance_runecraft
            4074233434L -> R.raw.advance_runecraft2
            3188758973L -> R.raw.advance_smithing
            3992599214 -> R.raw.advance_smithing2
            4116771415L -> R.raw.advance_strength //EOF EXC
            3980035869L -> R.raw.advance_strength2
            3277845279 -> R.raw.advance_thieving
            1822990819L -> R.raw.advance_thieving2
            3874942609L -> R.raw.advance_woodcutting
            203061075L -> R.raw.advance_woodcutting2
            4216160110L -> R.raw.death
            2051451258L -> R.raw.death2
            3918326379L -> R.raw.dice_lose
            150780502L -> R.raw.dice_win
            1016032513L -> R.raw.duel_start
            3379369073L -> R.raw.duel_win2
            1816818347L -> R.raw.quest_complete1
            1821706325L -> R.raw.quest_complete2
            1386621414L -> R.raw.quest_complete3
            2264456856L -> R.raw.sailing_journey
            2627289362L -> R.raw.treasure_hunt_win
            else -> {
                throw Exception("Missing Jingle: $crc")
            }
        }
        playAudioFromRaw(resource)
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

        player.volume = MainActivity.musicVolume.volume
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        playing = true
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    playing = false
                    player.release()
                    if (!Main.client.onlyPlayJingles())
                        MainActivity.lastSong?.let {
                            MainActivity.songPlayer?.release()
                            MainActivity.songPlayer = SongPlayer(it, context)
                        }
                }
            }
        })
    }


}