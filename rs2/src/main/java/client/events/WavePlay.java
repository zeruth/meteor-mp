package client.events;

import java.io.ByteArrayInputStream;

public class WavePlay {
    public static ByteArrayInputStream lastWave;
    public ByteArrayInputStream soundStream;
    public WavePlay(byte[] data, int pos) {
        soundStream = new ByteArrayInputStream(data, 0, pos);
        lastWave = new ByteArrayInputStream(data, 0, pos);
    }
}
