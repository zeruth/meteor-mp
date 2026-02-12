package jagex2.graphics;

import deob.ObfuscatedName;

public class PixMap {

	@ObfuscatedName("ISZGOOMR.c")
	public int width;

	@ObfuscatedName("ISZGOOMR.d")
	public int height;

	@ObfuscatedName("ISZGOOMR.b")
	public int[] data;

	public PixMap(int width, int height) {
		this.width = width;
		this.height = height;
		this.data = new int[height * width];
	}

	@ObfuscatedName("ISZGOOMR.a(Z)V")
	public void setPixels() {
		Pix2D.setPixels(this.data, this.width, this.height);
	}

	@ObfuscatedName("ISZGOOMR.a(IILjava/awt/Graphics;Z)V")
	public void draw(int y, int x) {}
}
