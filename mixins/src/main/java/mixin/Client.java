package mixin;

import net.runelite.api.Callbacks;
import net.runelite.api.mixins.*;
import net.runelite.rs.api.RSClient;
import net.runelite.rs.api.RSGameShell;

@SuppressWarnings("ALL")
@Mixin(RSClient.class)
abstract class Client implements RSClient {
    @Shadow("shell")
    public static RSGameShell shell;

    @Inject
    public Callbacks callbacks;

    @Inject
    @Override
    public Callbacks getCallbacks() {
        return callbacks;
    }

    @Inject
    @Override
    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Inject
    @Override
    public void publish(Object event) {
        callbacks.post(event);
    }
}
