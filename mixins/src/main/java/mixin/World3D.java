package mixin;

import net.runelite.api.Callbacks;
import net.runelite.api.events.PreTooltip;
import net.runelite.api.mixins.Copy;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.MethodHook;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.api.mixins.Shadow;
import net.runelite.rs.api.RSClient;
import net.runelite.rs.api.RSWorld3D;

@SuppressWarnings("ALL")
@Mixin(RSWorld3D.class)
abstract class World3D implements RSWorld3D {
    @Shadow("client")
    public static RSClient client;

    @Replace("occluded")
    @Inject
    private boolean occluded(int x, int y, int z) {
        return false;
    }
}
