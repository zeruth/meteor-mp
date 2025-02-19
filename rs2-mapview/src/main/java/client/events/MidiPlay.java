package client.events;

/**
 * Android and Desktop use a different scheme for [name] so be careful
 */
public class MidiPlay {
    public String name;
    public MidiPlay(String name) {
        this.name = name;
    }
}
