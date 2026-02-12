package meteor.platform.android

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import jagex3.sound.JavaPcmPlayer

class AndroidPcmPlayer : JavaPcmPlayer() {

    private var audioTrack: AudioTrack? = null
    private var channelCount = 1
    private var bufferSizeInBytes = 0

    override fun init() {
        channelCount = if (stereo) 2 else 1
        super.init()
    }

    override fun open(bufferSamples: Int) {
        if (audioTrack != null) {
            return
        }

        super.open(bufferSamples)

        channelCount = if (stereo) 2 else 1

        bufferSizeInBytes = (bufferSamples * 2 * channelCount).coerceAtLeast(
            AudioTrack.getMinBufferSize(
                frequency,
                if (stereo) AudioFormat.CHANNEL_OUT_STEREO else AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
        )


        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(frequency)
            .setChannelMask(if (stereo) AudioFormat.CHANNEL_OUT_STEREO else AudioFormat.CHANNEL_OUT_MONO)
            .build()

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(audioAttributes)
            .setAudioFormat(audioFormat)
            .setBufferSizeInBytes(bufferSizeInBytes)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        if (audioTrack?.state != AudioTrack.STATE_INITIALIZED) {
            println("AudioTrack failed to initialize!")
            return
        }

        audioTrack?.play()
    }

    var bytesWritten = 0

    override fun write(current: Int, goal: Int): Int {
        val size = super.write(current, goal)

        val bytesFromWrite = audioTrack!!.write(buffer, 0, size shl 1, AudioTrack.WRITE_BLOCKING)
        if (bytesFromWrite <= 0) {
            return size;
        }

        if (current + 256 < goal) {
            bytesWritten += bytesFromWrite;
        } else {
            bytesWritten = 0;
        }

        return size
    }

    override fun available(): Int {
        return bufferSizeInBytes / 2 / channelCount - bytesWritten
    }

    override fun flush() {
        audioTrack?.flush()
    }

    override fun close() {
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }
}
