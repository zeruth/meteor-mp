package mixin;

import net.runelite.api.Callbacks;
import net.runelite.api.events.PreTooltip;
import net.runelite.api.mixins.*;
import net.runelite.rs.api.RSClient;

@SuppressWarnings("ALL")
@Mixin(RSClient.class)
abstract class Client implements RSClient {
    @Shadow("client")
    public static RSClient client;

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

    @Copy("post")
    @Replace("post")
    public void post(Object event) {
        callbacks.post(event);
    }

    @MethodHook(value = "drawWildyLevel", end = true)
    @Inject
    private void drawWildyLevel$tail() {
        client.getCallbacks().post(PreTooltip.INSTANCE);
    }
}
