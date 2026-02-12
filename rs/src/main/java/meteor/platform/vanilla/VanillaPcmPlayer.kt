package meteor.platform.vanilla

import jagex3.sound.JavaPcmPlayer
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine

class VanillaPcmPlayer : JavaPcmPlayer() {
    var format: AudioFormat? = null
    var line: SourceDataLine? = null

    override fun init() {
        format = AudioFormat(frequency.toFloat(), 16, if (stereo) 2 else 1, true, false)
        super.init()
    }

    override fun open(arg0: Int) {
        try {
            val var2 = DataLine.Info(SourceDataLine::class.java, format, arg0 shl (if (stereo) 2 else 1))
            line = AudioSystem.getLine(var2) as SourceDataLine?
            line?.open()
            line?.start()
            super.open(arg0)
        } catch (_: Exception) {
            val var4 = arg0 - 1
            val var5 = var4 or (var4 ushr 1)
            val var6 = var5 or (var5 ushr 2)
            val var7 = var6 or (var6 ushr 4)
            val var8 = var7 or (var7 ushr 8)
            val var9 = var8 or (var8 ushr 16)
            val var10 = var9 + 1
            this.open(var10)
        }
    }

    override fun available(): Int {
        return line?.available() ?: -1
    }

    override fun write(current: Int, goal: Int): Int {
        val size = super.write(current, goal)
        var var1 = 256
        if (stereo) {
            var1 = var1 shl 0x1
        }
        line?.write(buffer, 0, var1 shl 1)
        return size
    }

    override fun close() {
        line?.close()
        line = null
    }

    override fun flush() {
        line?.flush()
    }
}