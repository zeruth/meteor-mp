package jagex3.graphics;

import deob.ObfuscatedName;

@ObfuscatedName("ab")
public abstract class PixMap {

	@ObfuscatedName("ab.r")
	public int[] data;

	@ObfuscatedName("ab.d")
	public int width;

	@ObfuscatedName("ab.l")
	public int height;

	@ObfuscatedName("ab.d(I)V")
	public final void bind() {
		Pix2D.setPixels(this.data, this.width, this.height);
	}

	@ObfuscatedName("ab.l(Ljava/awt/Graphics;III)V")
	public abstract void draw(int x, int y);

	@ObfuscatedName("ab.m(Ljava/awt/Graphics;IIIII)V")
	public abstract void draw(int x, int y, int w, int h);

	@ObfuscatedName("ab.r(IILjava/awt/Component;I)V")
	public abstract void create(int w, int h);
}
