package meteor.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import java.io.ByteArrayInputStream

class SoundPlayer(private val audioData: ByteArrayInputStream, private val delay: Int, private val sampleRate: Int = 22050) {
    private var audioTrack: AudioTrack? = null
    private var audioBuffer: ByteArray? = null

    fun loadAudioData() {
        audioBuffer = ByteArray(audioData.available()).also {
            audioData.read(it)
        }

        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_8BIT

        val bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat)

        audioTrack = AudioTrack(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(),
            AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setChannelMask(channelConfig)
                .setEncoding(audioFormat)
                .build(),
            bufferSize,
            AudioTrack.MODE_STREAM,
            0
        )
    }

    fun play() {
        Thread {
            loadAudioData()

            audioTrack?.play()

            if (delay > 0) {
                try {
                    Thread.sleep(delay.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            audioBuffer?.let { buffer ->
                audioTrack?.write(buffer, 0, buffer.size)
            }

            audioTrack?.stop()
            audioTrack?.release()
        }.start()
    }
}
