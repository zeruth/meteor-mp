package net.runelite.rs.api;

import net.runelite.api.Client;
import net.runelite.api.mixins.Inject;
import net.runelite.mapping.Import;

public interface RSClient extends Client, RSGameShell {
    @Import("setMidi")
    void setMidi$api(String name, int crc, int len);

    @Import("stopMidi")
    void stopMidi$api();

    @Import("ingame")
    boolean isLoggedIn();

    @Import("viewportInterfaceId")
    int getViewportInterfaceID();

    @Import("menuVisible")
    boolean isMenuVisible();

    @Import("members")
    void setMembers(boolean isMembers);

    @Import("updateServerConnection")
    void updateServerConnection$api(String URL, int PORT_OFFSET);

    @Import("keepUsername")
    void setKeepUsername(boolean keepUsername);

    @Import("keepPassword")
    void setKeepPassword(boolean keepPassword);

    @Import("autoUsername")
    void setAutoUsername(String autoUsername);

    @Import("autoPassword")
    void setAutoPassword(String autoPassword);

    @Import("orbitCameraYaw")
    int getCameraYaw();

    @Import("orbitCameraYaw")
    void setCameraYaw(int yaw);

    @Import("orbitCameraPitch")
    int getCameraPitch();

    @Import("orbitCameraPitch")
    void setCameraPitch(int yaw);

    @Import("skillBaseLevel")
    int[] getLevels();

    @Import("skillLevel")
    int[] getBoostedLevels();

    @Import("skillExperience")
    int[] getExperience();

    @Import("levelExperience")
    int[] getLevelExperience();

    @Import("energy")
    int getEnergy();

    @Import("menuX")
    int getMenuX();

    @Import("menuY")
    int getMenuY();

    @Import("menuWidth")
    int getMenuWidth();

    @Import("menuHeight")
    int getMenuHeight();

    @Import("packetType")
    int getPacketType();

    @Import("areaViewport")
    RSPixMap getAreaViewport();
}
