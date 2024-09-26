package com.meteor.android

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
            SoundPlayer(it.data.sound, 0).play()
        }
        KEVENT.subscribe<PlaySong> {
            mainHandler.post {
                val resource = when (it.data.song) {
                    "adventure" -> R.raw.adventure
                    "al_kharid" -> R.raw.al_kharid
                    "alone" -> R.raw.alone
                    "ambience_2" -> R.raw.ambience_2
                    "ambience_3" -> R.raw.ambience_2
                    "ambience_4" -> R.raw.ambience_4
                    "ambient_jungle" -> R.raw.ambient_jungle
                    "arabian" -> R.raw.arabian
                    "arabian2" -> R.raw.arabian2
                    "arabian3" -> R.raw.arabian3
                    "arabique" -> R.raw.arabique
                    "army_of_darkness" -> R.raw.army_of_darkness
                    "arrival" -> R.raw.arrival
                    "attack1" -> R.raw.attack1
                    "attack2" -> R.raw.attack2
                    "attack3" -> R.raw.attack3
                    "attack4" -> R.raw.attack4
                    "attack5" -> R.raw.attack5
                    "attack6" -> R.raw.attack6
                    "attention" -> R.raw.attention
                    "autumn_voyage" -> R.raw.autumn_voyage
                    "background2" -> R.raw.background2
                    "ballad_of_enchantment" -> R.raw.ballad_of_enchantment
                    "baroque" -> R.raw.baroque
                    "beyond" -> R.raw.beyond
                    "big_chords" -> R.raw.big_chords
                    "book_of_spells" -> R.raw.book_of_spells
                    "camelot" -> R.raw.camelot
                    "cave_background1" -> R.raw.cave_background1
                    "cavern" -> R.raw.cavern
                    "cellar_song1" -> R.raw.cellar_song1
                    "chain_of_command" -> R.raw.chain_of_command
                    "chompy_hunt" -> R.raw.chompy_hunt
                    "close_quarters" -> R.raw.close_quarters
                    "crystal_cave" -> R.raw.crystal_cave
                    "crystal_sword" -> R.raw.crystal_sword
                    "cursed" -> R.raw.cursed
                    "dangerous" -> R.raw.dangerous
                    "dark2" -> R.raw.dangerous
                    "deep_wildy" -> R.raw.deep_wildy
                    "desert_voyage" -> R.raw.desert_voyage
                    "doorways" -> R.raw.doorways
                    "dream1" -> R.raw.dream1
                    "duel_arena" -> R.raw.duel_arena
                    "dunjun" -> R.raw.dunjun
                    "egypt" -> R.raw.egypt
                    "emotion" -> R.raw.emotion
                    "emperor" -> R.raw.emperor
                    "escape" -> R.raw.escape
                    "expanse" -> R.raw.expanse
                    "expecting" -> R.raw.expecting
                    "expedition" -> R.raw.expedition
                    "fade_test" -> R.raw.fade_test
                    "faerie" -> R.raw.faerie
                    "fanfare" -> R.raw.fanfare
                    "fanfare2" -> R.raw.fanfare2
                    "fanfare3" -> R.raw.fanfare3
                    "fishing" -> R.raw.fishing
                    "flute_salad" -> R.raw.flute_salad
                    "forbidden" -> R.raw.forbidden
                    "forever" -> R.raw.forever
                    "game_intro_1" -> R.raw.game_intro_1
                    "gaol" -> R.raw.gaol
                    "garden" -> R.raw.garden
                    "gnome" -> R.raw.gnome
                    "gnome_king" -> R.raw.gnome_king
                    "gnome_theme" -> R.raw.gnome_theme
                    "gnome_village" -> R.raw.gnome_village
                    "gnome_village2" -> R.raw.gnome_village2
                    "gnomeball" -> R.raw.gnomeball
                    "greatness" -> R.raw.greatness
                    "grumpy" -> R.raw.grumpy
                    "harmony" -> R.raw.harmony
                    "harmony2" -> R.raw.harmony2
                    "heart_and_mind" -> R.raw.heart_and_mind
                    "high_seas" -> R.raw.high_seas
                    "horizon" -> R.raw.horizon
                    "iban" -> R.raw.iban
                    "ice_melody" -> R.raw.ice_melody
                    "in_the_manor" -> R.raw.in_the_manor
                    "inspiration" -> R.raw.inspiration
                    "intrepid" -> R.raw.intrepid
                    "jolly-r" -> R.raw.jolly_r
                    "jungle_island" -> R.raw.jungle_island
                    "jungly1" -> R.raw.jungly1
                    "jungly2" -> R.raw.jungly2
                    "jungly3" -> R.raw.jungly3
                    "knightly" -> R.raw.knightly
                    "landlubber" -> R.raw.landlubber
                    "lasting" -> R.raw.lasting
                    "legion" -> R.raw.legion
                    "lightness" -> R.raw.lightness
                    "lightwalk" -> R.raw.lightwalk
                    "lonesome" -> R.raw.lonesome
                    "long_ago" -> R.raw.long_ago
                    "long_way_home" -> R.raw.long_way_home
                    "lullaby" -> R.raw.lullaby
                    "mage_arena" -> R.raw.mage_arena
                    "magic_dance" -> R.raw.magic_dance
                    "magical_journey" -> R.raw.magical_journey
                    "march2" -> R.raw.march2
                    "medieval" -> R.raw.medieval
                    "mellow" -> R.raw.mellow
                    "miles_away" -> R.raw.miles_away
                    "miracle_dance" -> R.raw.miracle_dance
                    "monarch_waltz" -> R.raw.monarch_waltz
                    "moody" -> R.raw.moody
                    "neverland" -> R.raw.neverland
                    "newbie_melody" -> R.raw.newbie_melody
                    "nightfall" -> R.raw.nightfall
                    "nomad" -> R.raw.nomad
                    "null" -> R.raw.null_
                    "organ_music_1" -> R.raw.organ_music_1
                    "organ_music_2" -> R.raw.organ_music_2
                    "oriental" -> R.raw.oriental
                    "overture" -> R.raw.overture
                    "parade" -> R.raw.parade
                    "quest" -> R.raw.quest
                    "regal2" -> R.raw.regal2
                    "reggae" -> R.raw.reggae
                    "reggae2" -> R.raw.reggae2
                    "riverside" -> R.raw.riverside
                    "royale" -> R.raw.royale
                    "rune_essence" -> R.raw.rune_essence
                    "sad_meadow" -> R.raw.sad_meadow
                    "scape_cave" -> R.raw.scape_cave
                    "scape_main" -> R.raw.scape_main
                    "scape_sad1" -> R.raw.scape_sad1
                    "scape_soft" -> R.raw.scape_soft
                    "scape_wild1" -> R.raw.scape_wild1
                    "sea_shanty" -> R.raw.sea_shanty
                    "sea_shanty2" -> R.raw.sea_shanty2
                    "serenade" -> R.raw.serenade
                    "serene" -> R.raw.serene
                    "shine" -> R.raw.shine
                    "shining" -> R.raw.shining
                    "silence" -> R.raw.silence
                    "soundscape" -> R.raw.soundscape
                    "spirit" -> R.raw.spirit
                    "splendour" -> R.raw.splendour
                    "spooky2" -> R.raw.spooky2
                    "spooky_jungle" -> R.raw.spooky_jungle
                    "starlight" -> R.raw.starlight
                    "start" -> R.raw.start
                    "still_night" -> R.raw.still_night
                    "talking_forest" -> R.raw.talking_forest
                    "the_desert" -> R.raw.the_desert
                    "the_shadow" -> R.raw.the_shadow
                    "the_tower" -> R.raw.the_tower
                    "theme" -> R.raw.theme
                    "tomorrow" -> R.raw.tomorrow
                    "trawler" -> R.raw.trawler
                    "trawler_minor" -> R.raw.trawler_minor
                    "tree_spirits" -> R.raw.tree_spirits
                    "tribal" -> R.raw.tribal
                    "tribal2" -> R.raw.tribal2
                    "tribal_background" -> R.raw.tribal_background
                    "trinity" -> R.raw.trinity
                    "troubled" -> R.raw.troubled
                    "undercurrent" -> R.raw.undercurrent
                    "underground" -> R.raw.underground
                    "understanding" -> R.raw.understanding
                    "unknown_land" -> R.raw.unknown_land
                    "upass1" -> R.raw.upass1
                    "upcoming" -> R.raw.upcoming
                    "venture" -> R.raw.venture
                    "venture2" -> R.raw.venture2
                    "vision" -> R.raw.vision
                    "voodoo_cult" -> R.raw.voodoo_cult
                    "voyage" -> R.raw.voyage
                    "wander" -> R.raw.wander
                    "waterfall" -> R.raw.waterfall
                    "wilderness2" -> R.raw.wilderness2
                    "wilderness3" -> R.raw.wilderness3
                    "wilderness4" -> R.raw.wilderness4
                    "witching" -> R.raw.witching
                    "wolf_mountain" -> R.raw.wolf_mountain
                    "wonder" -> R.raw.wonder
                    "wonderous" -> R.raw.wonderous
                    "workshop" -> R.raw.workshop
                    "yesteryear" -> R.raw.yesteryear
                    "zealot" -> R.raw.zealot
                    else -> {
                        println("Missing Song: " + it.data.song)
                        return@post
                    }
                }
                playAudioFromRaw(resource)
            }
        }
        KEVENT.subscribe<StopMusic> {
            mainHandler.post {
                release()
            }
        }
        Thread {
            while (!receivedDraw) {
                updateGameImage(false)
            }
        }.start()
    }

    // Initialize the player
    var player: ExoPlayer? = null


    fun playAudioFromRaw(resourceId: Int) {
        if (player != null) {
            player?.release()
            player = null
        }

        player = ExoPlayer.Builder(applicationContext).build()

        // Create a media item from the raw resource
        val uri = Uri.parse("rawresource://" + applicationContext.packageName + "/" + resourceId)
        val mediaItem = MediaItem.fromUri(uri)

        // Prepare the player
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }

    fun release() {
        player?.release()
        player = null
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