package jagex3.client;

import deob.ObfuscatedName;

@ObfuscatedName("fk")
public class GameCanvas {
	public GameCanvas() {}

	public final void update() {
		GameShell.context.update();
	}

	public final void paint() {
		GameShell.context.paint();
	}
}
