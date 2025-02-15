package net.runelite.rs.api;

import net.runelite.api.GameShell;
import net.runelite.mapping.Import;

import java.awt.event.KeyEvent;

public interface RSGameShell extends GameShell {
    @Import("initApplication")
    void initApplication$api( int width, int height);

    @Import("mouseReleased")
    void mouseReleased$api();

    @Import("mouseReleased")
    void mouseReleased$api(boolean metaDown);

    @Import("mouseReleased")
    void mouseReleased$api(int i, boolean metaDown);

    @Import("mousePressed")
    void mousePressed$api(int x, int y, int button, boolean isMetaDown);

    @Import("keyPressed")
    void keyPressed$api(KeyEvent e);

    @Import("keyReleased")
    void keyReleased$api(KeyEvent e);

    @Import("keyPressed")
    void keyPressed$api(int i, int k);

    @Import("keyReleased")
    void keyReleased$api(int i);

    @Import("mouseMoved")
    void mouseMoved$api(int x, int y);

    @Import("keyTyped")
    void keyTyped$api(int i);
}
