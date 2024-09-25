package net.runelite.api;

import java.awt.Graphics;
import java.awt.Image;

public interface PixMap {
    void draw$api(Graphics g, int x, int y);
    Image getImage();
}
