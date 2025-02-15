package net.runelite.rs.api;

import net.runelite.api.GameShell;
import net.runelite.api.PixMap;
import net.runelite.mapping.Import;

import java.awt.event.KeyEvent;

public interface RSPixMap extends PixMap {
    @Import("pixels")
    int[] getPixels();

    @Import("width")
    int getWidth();

    @Import("height")
    int getHeight();
}
