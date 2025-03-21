package net.runelite.api;

import net.runelite.mapping.Import;

import java.awt.event.KeyEvent;

public interface Client extends GameShell {
    Callbacks getCallbacks();

    void setCallbacks(Callbacks callbacks);

    boolean isLoggedIn();

    String getUsername();

    String getPassword();

    void setPassword(String password);

    void setUsername(String username);

    boolean inGame();

    void mouseMoved$api(int x, int y);
    void mouseReleased$api();
    void mouseReleased$api(boolean metaDown);

    void mouseReleased$api(int i, boolean metaDown);
    void mousePressed$api(int x, int y, int button, boolean isMetaDown);
    void keyPressed$api(KeyEvent e);
    void keyPressed$api(int i, int k);
    void keyReleased$api(KeyEvent e);
    void keyReleased$api(int i);
    void keyTyped$api(int i);

    int getSceneState();

    PixMap getAreaViewport();

    int getCameraPitch();
    int getCameraYaw();
    void setCameraPitch(int pitch);
    void setCameraYaw(int yaw);
    String[] getMenuOptions();
    void setMenuOptions(String[] options);
    int getMenuSize();
    void setMenuSize(int size);
    int[] getMenuActions();
    void setMenuActions(int[] actions);
    int[] getMenuParamAs();
    void setMenuParamAs(int[] paramAs);
    int[] getMenuParamBs();
    void setMenuParamBs(int[] paramBs);
    int[] getMenuParamCs();
    void setMenuParamCs(int[] paramCs);
    boolean getShiftPressed();
    void setShiftPressed(boolean pressed);
    int getOrbitCameraZoom();
    void setOrbitCameraZoom(int zoom);
}
