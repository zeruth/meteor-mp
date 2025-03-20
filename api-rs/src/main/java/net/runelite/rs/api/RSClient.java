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

    @Import("orbitCameraPitch")
    int getCameraPitch();

    @Import("orbitCameraYaw")
    int getCameraYaw();

    @Import("orbitCameraPitch")
    void setCameraPitch(int pitch);

    @Import("orbitCameraYaw")
    void setCameraYaw(int yaw);

    @Import("menuOption")
    String[] getMenuOptions();

    @Import("menuOption")
    void setMenuOptions(String[] options);

    @Import("menuSize")
    int getMenuSize();

    @Import("menuSize")
    void setMenuSize(int size);

    @Import("menuAction")
    int[] getMenuActions();

    @Import("menuAction")
    void setMenuActions(int[] actions);

    @Import("menuParamA")
    int[] getMenuParamAs();

    @Import("menuParamA")
    void setMenuParamAs(int[] paramAs);

    @Import("menuParamB")
    int[] getMenuParamBs();

    @Import("menuParamB")
    void setMenuParamBs(int[] paramBs);

    @Import("menuParamC")
    int[] getMenuParamCs();

    @Import("menuParamC")
    void setMenuParamCs(int[] paramCs);

    @Import("shiftPressed")
    boolean getShiftPressed();

    @Import("shiftPressed")
    void setShiftPressed(boolean pressed);
}
