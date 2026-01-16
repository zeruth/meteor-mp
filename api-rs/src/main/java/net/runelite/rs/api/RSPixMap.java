package net.runelite.rs.api;

import net.runelite.api.PixMap;
import net.runelite.mapping.Import;

public interface RSPixMap extends PixMap {
    @Import("data")
    int[] getData();

    @Import("width")
    int getWidth();

    @Import("height")
    int getHeight();
}
