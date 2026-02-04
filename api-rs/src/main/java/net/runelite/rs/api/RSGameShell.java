package net.runelite.rs.api;

import net.runelite.api.GameShell;
import net.runelite.mapping.Import;

import java.awt.event.KeyEvent;

public interface RSGameShell extends GameShell {
    @Import("initApplication")
    void initApplication$api( int width, int height);
}
