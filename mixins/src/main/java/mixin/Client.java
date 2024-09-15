package mixin;

import net.runelite.api.Callbacks;
import net.runelite.api.Component;
import net.runelite.api.PacketTypeServer;
import net.runelite.api.mixins.*;
import net.runelite.rs.api.RSClient;
import net.runelite.rs.api.RSComponent;

import meteor.events.ClientInstance;
import meteor.events.DrawFinished;
import meteor.events.SkillUpdate;
import meteor.ui.config.AspectMode;
import meteor.ui.config.CPUFilter;

@SuppressWarnings("ALL")
@Mixin(RSClient.class)
abstract class Client implements RSClient {
    @Shadow("client")
    public static RSClient client;

    @Shadow("instances")
    public static RSComponent[] components;

    @Shadow("hooks")
    public static Object hooks;

    @Inject
    @Override
    public Callbacks getCallbacks() {
        return (Callbacks) hooks;
    }

    @Inject
    @MethodHook(value = "draw", end = true)
    public void draw$tail() {
        getCallbacks().post(DrawFinished.INSTANCE);
    }

    @Inject
    @MethodHook(value = "announceInstance", end = true)
    public void announceInstance$tail() {
        getCallbacks().post(ClientInstance.INSTANCE);
    }

    @Inject
    private AspectMode aspectMode;

    @Inject
    @Override
    public AspectMode getAspectMode() {
        if (aspectMode == null)
            aspectMode = AspectMode.FIT;
        return aspectMode;
    }

    @Inject
    @Override
    public void setAspectMode(AspectMode aspectMode) {
        this.aspectMode = aspectMode;
    }

    @Inject
    private CPUFilter cpuFilter = CPUFilter.NONE;

    @Inject
    @Override
    public CPUFilter getCPUFilter() {
        if (cpuFilter == null)
            cpuFilter = CPUFilter.NONE;
        return cpuFilter;
    }

    @Inject
    @Override
    public void setCPUFilter(CPUFilter cpuFilter) {
        this.cpuFilter = cpuFilter;
    }


    @Inject
    private float stretchedWidth = -1f;

    @Inject
    @Override
    public float getStretchedWidth() {
        return stretchedWidth;
    }

    @Inject
    @Override
    public void setStretchedWidth(float stretchedWidth) {
        this.stretchedWidth = stretchedWidth;
    }

    @Inject
    private float stretchedHeight = -1f;

    @Inject
    @Override
    public float getStretchedHeight() {
        return stretchedHeight;
    }

    @Inject
    @Override
    public void setStretchedHeight(float stretchedHeight) {
        this.stretchedHeight = stretchedHeight;
    }

    @Inject
    private float xPadding = 0;

    @Inject
    @Override
    public float getXPadding() {
        if (xPadding == -1)
            xPadding = 0;
        return xPadding;
    }

    @Inject
    @Override
    public void setXPadding(float padding) {
        this.xPadding = padding;
    }

    @Inject
    private float yPadding = 0;

    @Inject
    @Override
    public float getYPadding() {
        if (yPadding == -1)
            yPadding = 0;
        return yPadding;
    }

    @Inject
    @Override
    public void setYPadding(float padding) {
        this.yPadding = padding;
    }

    @Inject
    @Override
    public Component[] getComponents() {
        return components;
    }

    @Inject
    public int lastPacketTypeServer = -1;

    @Inject
    @FieldHook(value = "packetType")
    public void packetType$tail(int idx) {
        int nextPacketType = getPacketType();

        // done processing last packet
        if (nextPacketType == -1) {
            if (lastPacketTypeServer == PacketTypeServer.UPDATE_STAT.id) {
                client.getCallbacks().post(new SkillUpdate(getLevels(), getBoostedLevels(), getExperience()));
            }
        }

        lastPacketTypeServer = nextPacketType;
    }
}
