package net.runelite.rs.api;

import net.runelite.api.GameShell;
import net.runelite.mapping.Import;

public interface RSGameShell extends GameShell, Runnable {
    @Import("screenWidth")
    void setScreenWidth(int screenWidth);

    @Import("screenHeight")
    void setScreenHeight(int screenHeight);
}
