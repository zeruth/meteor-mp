package meteor.events

class MidiPlayerPlay(
    @JvmField var file: java.io.File,
    @JvmField var fade: Int,
    @JvmField var vol: Int)