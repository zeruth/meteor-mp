package jagex3.client.input.mouse;

import deob.ObfuscatedName;
import jagex3.client.GameShell;


@ObfuscatedName("dh")
public class ClientMouseWheelListener extends MouseWheelInterface {

	@ObfuscatedName("dh.r")
	public int rotation = 0;

	@ObfuscatedName("dh.d(Ljava/awt/Component;I)V")
	public void addListeners() {
		GameShell.context.addMouseWheelListener(this);
	}

	@ObfuscatedName("dh.l(Ljava/awt/Component;B)V")
	public void removeListeners() {
		GameShell.context.removeMouseWheelListener(this);
	}

	@ObfuscatedName("dh.m(I)I")
	public synchronized int getRotation() {
		int total = this.rotation;
		this.rotation = 0;
		return total;
	}
}
