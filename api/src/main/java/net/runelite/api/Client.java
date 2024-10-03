package net.runelite.api;
import net.runelite.mapping.Import;

import meteor.ui.config.AspectMode;
import meteor.ui.config.CPUFilter;

public interface Client extends GameShell{
    Callbacks getCallbacks();

    void setCallbacks(Callbacks callbacks);

    boolean isLoggedIn();

    AspectMode getAspectMode();

    void setAspectMode(AspectMode aspectMode);

    float getXPadding();

    void setXPadding(float xPadding);
    float getYPadding();

    void setYPadding(float xPadding);

    CPUFilter getCPUFilter();

    void setCPUFilter(CPUFilter cpuFilter);

    int getViewportInterfaceID();

    Component[] getComponents();

    int getMenuX();
    int getMenuY();
    int getMenuWidth();
    int getMenuHeight();
    boolean getMenuVisible();


    float getStretchedWidth();

    void setStretchedWidth(float stretcheWidth);

    float getStretchedHeight();

    void setStretchedHeight(float stretcheHeight);

    void setMembers(boolean isMembers);
    void updateServerConnection$api(String URL, int PORT_OFFSET);

    void setMidi$api(String name, int crc, int len);
    void stopMidi$api();

    void setAutoUsername(String lastUsername);
    void setAutoPassword(String lastPassword);
    void setKeepUsername(boolean keepUsername);
    void setKeepPassword(boolean keepPassword);
    int getCameraYaw();
    void setCameraYaw(int yaw);
    int getCameraPitch();
    void setCameraPitch(int yaw);
    int[] getLevels();
    int[] getBoostedLevels();
    int[] getExperience();
    int[] getLevelExperience();
    int getEnergy();
    PixMap getAreaViewport();
    String getMidi();
    boolean isBankVisible();
}
