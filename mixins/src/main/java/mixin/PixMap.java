package mixin;

import net.runelite.api.Callbacks;
import net.runelite.api.Component;
import net.runelite.api.PacketTypeServer;
import net.runelite.api.mixins.Copy;
import net.runelite.api.mixins.FieldHook;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.MethodHook;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.api.mixins.Shadow;
import net.runelite.rs.api.RSClient;
import net.runelite.rs.api.RSComponent;
import net.runelite.rs.api.RSPixMap;

import java.awt.Graphics;

import meteor.events.ClientInstance;
import meteor.events.DrawFinished;
import meteor.events.SkillUpdate;
import meteor.events.ViewportDraw;
import meteor.ui.config.AspectMode;
import meteor.ui.config.CPUFilter;

@SuppressWarnings("ALL")
@Mixin(RSPixMap.class)
abstract class PixMap implements RSPixMap {
    @Shadow("client")
    public static RSClient client;

    @Copy("draw")
    @Replace("draw")
    public void draw(Graphics g, int x, int y) {
        if (this == client.getAreaViewport()) {
            client.getCallbacks().post(ViewportDraw.INSTANCE);
        } else {
            draw(g, x, y);
        }
    }
}
