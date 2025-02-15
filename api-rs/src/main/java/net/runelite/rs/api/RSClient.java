package net.runelite.rs.api;

import net.runelite.api.Client;
import net.runelite.api.mixins.Inject;
import net.runelite.mapping.Import;

import java.awt.event.KeyEvent;

public interface RSClient extends Client, RSGameShell {
    @Import("username")
    String getUsername();

    @Import("username")
    void setUsername(String username);

    @Import("password")
    String getPassword();

    @Import("password")
    void setPassword(String password);

    @Import("ingame")
    boolean inGame();

    @Import("sceneState")
    int getSceneState();

    @Import("areaViewport")
    RSPixMap getAreaViewport();
}
