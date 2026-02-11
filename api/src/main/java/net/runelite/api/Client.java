package net.runelite.api;

public interface Client extends GameShell {
    Callbacks getCallbacks();

    void setCallbacks(Callbacks callbacks);

    void publish(Object event);
}
