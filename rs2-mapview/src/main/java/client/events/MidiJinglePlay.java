package client.events;
public class MidiJinglePlay {
    public long crc;
    public boolean consumed = false;
    public MidiJinglePlay(long crc) {
        this.crc = crc;
    }

    public boolean getConsumend() {
        return consumed;
    }

    public void consume() {
        this.consumed = true;
    }
}
