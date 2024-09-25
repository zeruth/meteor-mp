package net.runelite.rs.api;

import net.runelite.api.Component;
import net.runelite.api.PixMap;
import net.runelite.mapping.Import;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public interface RSPixMap extends PixMap {
    @Import("draw")
    @Override
    void draw$api(Graphics g, int x, int y);

    @Import("image")
    @Override
    Image getImage();
}
