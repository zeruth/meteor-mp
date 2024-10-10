package meteor.audio

import JinglePlayer
import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.meteor.android.MainActivity
import com.meteor.android.R
import meteor.Main
import net.runelite.api.VolumeSetting

class SongPlayer(songName: String, private val context: Context) {
    var player = ExoPlayer.Builder(context).build()

    init {
        if (!JinglePlayer.playing) {
            val resource = when (songName) {
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
                    throw Exception("Missing Song: $songName")
                }
            }
            playAudioFromRaw(resource)
        }
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
        player.volume = if (Main.client.isLoggedIn) MainActivity.musicVolume.volume else VolumeSetting.FOUR.volume
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }


}