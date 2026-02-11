package jagex3.client.input.mouse;

import deob.ObfuscatedName;
import jagex3.client.GameShell;

// jag::oldscape::input::ClientMouseListener
@ObfuscatedName("an")
public class ClientMouseListener {

	@ObfuscatedName("an.r")
	public static ClientMouseListener instance = new ClientMouseListener();

	@ObfuscatedName("an.d")
	public static volatile int idleTimer = 0;

	@ObfuscatedName("an.l")
	public static volatile int nextMouseButton = 0;

	@ObfuscatedName("an.m")
	public static volatile int nextMouseX = -1;

	@ObfuscatedName("an.c")
	public static volatile int nextMouseY = -1;

	@ObfuscatedName("an.n")
	public static int mouseButton = 0;

	@ObfuscatedName("an.j")
	public static int mouseX = 0;

	@ObfuscatedName("an.z")
	public static int mouseY = 0;

	@ObfuscatedName("an.g")
	public static volatile int nextMouseClickButton = 0;

	@ObfuscatedName("an.q")
	public static volatile int nextMouseClickX = 0;

	@ObfuscatedName("an.i")
	public static volatile int nextMouseClickY = 0;

	@ObfuscatedName("an.s")
	public static volatile long nextMouseClickTime = 0L;

	@ObfuscatedName("an.u")
	public static int mouseClickButton = 0;

	@ObfuscatedName("an.v")
	public static int mouseClickX = 0;

	@ObfuscatedName("an.w")
	public static int mouseClickY = 0;

	@ObfuscatedName("an.e")
	public static long mouseClickTime = 0L;

	@ObfuscatedName("v.r(Ljava/awt/Component;I)V")
	public static void addListeners() {
		GameShell.context.addMouseListeners();
	}

	public static void removeListeners() {
		GameShell.context.removeMouseListeners();
		nextMouseButton = 0;
	}

	@ObfuscatedName("ek.d(II)V")
	public static void setIdleTimer(int v) {
		idleTimer = v;
	}

	public static void cycle() {
		ClientMouseListener lock = instance;
		synchronized (lock) {
			mouseButton = nextMouseButton;
			mouseX = nextMouseX;
			mouseY = nextMouseY;
			mouseClickButton = nextMouseClickButton;
			mouseClickX = nextMouseClickX;
			mouseClickY = nextMouseClickY;
			mouseClickTime = nextMouseClickTime;

			nextMouseClickButton = 0;
		}
	}

	public static void shutdown() {
		if (instance != null) {
			ClientMouseListener lock = instance;
			synchronized (lock) {
				instance = null;
			}
		}
	}

	public static int getIdleTimer() {
		return idleTimer++;
	}
}
