package meteor.context.platform.vanilla

import jagex2.client.Client
import jagex2.graphics.Pix32
import meteor.context.PlatformContext
import meteor.context.events.*
import sign.signlink
import util.GlobalEventBus
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.sound.midi.MidiSystem
import javax.sound.sampled.*

open class VanillaPlatform : PlatformContext() {
    init {
        viewBoxImplementation = VanillaViewBox::class
        pixMapImplementation = VanillaPixMap::class

        GlobalEventBus.subscribe<MidiPlayerRunning> {
            it.payload.running = vanillaMidiPlayer.running()
        }

        GlobalEventBus.subscribe<MidiPlayerPlay> {
            vanillaMidiPlayer.play(MidiSystem.getSequence(it.payload.file), it.payload.fade, it.payload.vol)
        }

        GlobalEventBus.subscribe<MidiPlayerSetVolume> {
            vanillaMidiPlayer.setVolume(it.payload.velocity, it.payload.volume)
        }

        GlobalEventBus.subscribe<MidiPlayerStop> {
            vanillaMidiPlayer.stop()
        }

        GlobalEventBus.subscribe<WavePlay> {
            val audioInputStream: AudioInputStream
            try {
                audioInputStream = AudioSystem.getAudioInputStream(File(signlink.wave))
            } catch (ignore: Exception) {
                return@subscribe
            }

            val format: AudioFormat? = audioInputStream.getFormat()
            val auline: SourceDataLine
            val info: DataLine.Info = DataLine.Info(SourceDataLine::class.java, format)

            try {
                auline = AudioSystem.getLine(info) as SourceDataLine
                auline.open(format)
            } catch (ignore: Exception) {
                return@subscribe
            }

            if (auline.isControlSupported(FloatControl.Type.PAN)) {
                val pan: FloatControl = auline.getControl(FloatControl.Type.PAN) as FloatControl
                if (it.payload.curPosition == signlink.Position.RIGHT) {
                    pan.setValue(1.0f)
                } else if (it.payload.curPosition == signlink.Position.LEFT) {
                    pan.setValue(-1.0f)
                }
            }

            auline.start()
            var nBytesRead = 0
            val EXTERNAL_BUFFER_SIZE = 524288
            val abData = ByteArray(EXTERNAL_BUFFER_SIZE)
            try {
                while (nBytesRead != -1) {
                    nBytesRead = audioInputStream.read(abData, 0, abData.size)
                    if (nBytesRead >= 0) {
                        auline.write(abData, 0, nBytesRead)
                    }
                }
            } catch (ignore: IOException) {
            } finally {
                auline.drain()
                auline.close()
            }
        }

        GlobalEventBus.subscribe<DrawProgress> {
            drawProgress(it.payload.progress, it.payload.message)
        }
    }

    companion object {
        private var vanillaMidiPlayer = VanillaMidiPlayer()
        val wipeCache: Boolean = false

        init {
            getCacheDirImpl = ::getCacheDirImpl
        }

        fun getCacheDirImpl(): String {
            val s = System.getProperty("user.home") + "/meteor-377/cache/"
            println("[Cache] " + s.replace("/", "\\"))
            val f = File(s)
            if (wipeCache && f.exists()) {
                try {
                    signlink.deleteRecursively(f)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
                if (!f.exists()) {
                    println("Wiped Cache!")
                }
            }

            if (!f.exists() && !f.mkdirs()) {
                try {
                    throw IOException("Failed to create cache directory: " + f.absolutePath)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }

            return s
        }
    }

    fun getGraphics(): Graphics? {
        return (viewbox as VanillaViewBox).getGraphics()
    }

    open fun drawProgress(progress: Int, message: String?) {
        val client = this as Client
        val graphics = getGraphics() ?: return
        try {
            val bold = Font("Helvetica", Font.BOLD, 13)
            val boldMetrics: FontMetrics = this.baseComponent.getFontMetrics(bold)

            val plain = Font("Helvetica", Font.PLAIN, 13)
            val plainMetrics: FontMetrics = this.baseComponent.getFontMetrics(plain)

            if (this.redrawScreen) {
                graphics.color = Color.black
                graphics.fillRect(0, 0, this.canvasWidth, this.canvasHeight)
                this.redrawScreen = false
            }

            val background = Color(140, 17, 17)

            val y: Int = this.canvasHeight / 2 - 18
            graphics.color = background
            graphics.drawRect(this.canvasWidth / 2 - 152, y, 304, 34)
            graphics.fillRect(this.canvasWidth / 2 - 150, y + 2, progress * 3, 30)

            graphics.color = Color.black
            graphics.fillRect(progress * 3 + (this.canvasWidth / 2 - 150), y + 2, 300 - progress * 3, 30)

            graphics.font = bold
            graphics.color = Color.white
            graphics.drawString(message, (this.canvasWidth - boldMetrics.stringWidth(message)) / 2, y + 22)
        } catch (e: java.lang.Exception) {
            // This will fail on headless platform, so ignore
        }
    }

    override fun createPix32(src: ByteArray): Pix32 {
        val img: BufferedImage = ImageIO.read(ByteArrayInputStream(src))
            ?: throw IllegalArgumentException("Invalid image data")

        val wi = img.width
        val hi = img.height
        val pixels = IntArray(wi * hi)
        img.getRGB(0, 0, wi, hi, pixels, 0, wi)

        return Pix32().apply {
            this.wi = wi
            this.hi = hi
            this.owi = wi
            this.ohi = hi
            this.xof = 0
            this.yof = 0
            this.pixels = pixels
        }
    }

    override fun drawError() {
        val client = this as Client
        getGraphics()?.let { var2 ->
            var2.color = Color.black;
            var2.fillRect(0, 0, 765, 503);
            client.setFramerate(1);
            if (client.errorLoading) {
                client.flameActive = false;
                var2.font = Font("Helvetica", 1, 16);
                var2.color = Color.yellow;
                val var4 = 35;
                var2.drawString("Sorry, an error has occured whilst loading RuneScape", 30, var4);
                val var6 = var4 + 50;
                var2.color = Color.white;
                var2.drawString("To fix this try the following (in order):", 30, var6);
                val var7 = var6 + 50;
                var2.color = Color.white;
                var2.font = Font("Helvetica", 1, 12);
                var2.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, var7);
                val var8 = var7 + 30;
                var2.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30, var8);
                val var9 = var8 + 30;
                var2.drawString("3: Try using a different game-world", 30, var9);
                val var11 = var9 + 30;
                var2.drawString("4: Try rebooting your computer", 30, var11);
                val var13 = var11 + 30;
                var2.drawString("5: Try selecting a different version of Java from the play-game menu", 30, var13);
            }
            if (client.errorHost) {
                client.flameActive = false;
                var2.font = Font("Helvetica", 1, 20);
                var2.color = Color.white;
                var2.drawString("Error - unable to load game!", 50, 50);
                var2.drawString("To play RuneScape make sure you play from", 50, 100);
                var2.drawString("http://www.runescape.com", 50, 150);
            }
            if (client.errorStarted) {
                client.flameActive = false;
                var2.color = Color.yellow;
                val var5 = 35;
                var2.drawString("Error a copy of RuneScape already appears to be loaded", 30, var5);
                val var10 = var5 + 50;
                var2.color = Color.white;
                var2.drawString("To fix this try the following (in order):", 30, var10);
                val var12 = var10 + 50;
                var2.color = Color.white;
                var2.font = Font("Helvetica", 1, 12);
                var2.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, var12);
                val var14 = var12 + 30;
                var2.drawString("2: Try rebooting your computer, and reloading", 30, var14);
                val var15 = var14 + 30;
            }
        }
    }
}