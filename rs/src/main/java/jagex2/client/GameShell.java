package jagex2.client;

import deob.ObfuscatedName;
import jagex2.graphics.Pix32;
import jagex2.graphics.PixMap;
import meteor.context.PlatformContext;
import meteor.context.platform.android.AndroidPlatform;
import meteor.context.platform.headless.AndroidHeadlessPlatform;
import org.jetbrains.annotations.NotNull;

public class GameShell extends AndroidPlatform implements Runnable {

	public static boolean isAndroid = false;

	//---

	@ObfuscatedName("JWWAIQPI.g")
	public int deltime = 20;

	@ObfuscatedName("JWWAIQPI.h")
	public int mindel = 1;

	@ObfuscatedName("JWWAIQPI.i")
	public long[] otim = new long[10];

	@ObfuscatedName("JWWAIQPI.k")
	public boolean debug = false;

	@ObfuscatedName("JWWAIQPI.p")
	public Pix32[] temp = new Pix32[6];

	@ObfuscatedName("JWWAIQPI.r")
	public boolean redrawScreen = true;

	@ObfuscatedName("JWWAIQPI.s")
	public boolean hasFocus = true;

	@ObfuscatedName("JWWAIQPI.F")
	public int[] actionKey = new int[128];

	@ObfuscatedName("JWWAIQPI.G")
	public int[] keyQueue = new int[128];

	@ObfuscatedName("JWWAIQPI.f")
	public int state;

	@ObfuscatedName("JWWAIQPI.j")
	public int fps;

	@ObfuscatedName("JWWAIQPI.l")
	public int canvasWidth;

	@ObfuscatedName("JWWAIQPI.m")
	public int canvasHeight;

	@ObfuscatedName("JWWAIQPI.t")
	public int idleCycles;

	@ObfuscatedName("JWWAIQPI.u")
	public int mouseButton;

	@ObfuscatedName("JWWAIQPI.v")
	public int mouseX;

	@ObfuscatedName("JWWAIQPI.w")
	public int mouseY;

	@ObfuscatedName("JWWAIQPI.x")
	public int nextMouseClickButton;

	@ObfuscatedName("JWWAIQPI.y")
	public int nextMouseClickX;

	@ObfuscatedName("JWWAIQPI.z")
	public int nextMouseClickY;

	@ObfuscatedName("JWWAIQPI.B")
	public int mouseClickButton;

	@ObfuscatedName("JWWAIQPI.C")
	public int mouseClickX;

	@ObfuscatedName("JWWAIQPI.D")
	public int mouseClickY;

	@ObfuscatedName("JWWAIQPI.H")
	public int keyQueueReadPos;

	@ObfuscatedName("JWWAIQPI.I")
	public int keyQueueWritePos;

	@ObfuscatedName("JWWAIQPI.A")
	public long nextMouseClickTime;

	@ObfuscatedName("JWWAIQPI.E")
	public long mouseClickTime;

	@ObfuscatedName("JWWAIQPI.q")
	public ViewBox frame;

	@ObfuscatedName("JWWAIQPI.o")
	public PixMap drawArea;

	@ObfuscatedName("JWWAIQPI.a(III)V")
	public void initApplication(int height, int width) {
		this.canvasWidth = width;
		this.canvasHeight = height;

		System.out.println("here");
		this.frame = createViewBox(canvasWidth, canvasHeight);

		System.out.println("here1");
		setViewbox(frame);
		this.drawArea = createPixmap(this.canvasWidth, this.canvasHeight);

		System.out.println("here");
		this.startThread(this, 1);
	}

	public void run() {

/*		if (this.frame != null) {
			this.frame.addWindowListener(this);
		}*/

		this.drawProgress(0, "Loading...");
		this.load();

		int opos = 0;
		int ratio = 256;
		int delta = 1;
		int count = 0;
		int intex = 0;

		for (int i = 0; i < 10; i++) {
			this.otim[i] = System.currentTimeMillis();
		}

		long ntime = System.currentTimeMillis();
		while (this.state >= 0) {
			if (this.state > 0) {
				this.state--;

				if (this.state == 0) {
					this.shutdown();
					return;
				}
			}

			int lastRatio = ratio;
			int lastDelta = delta;

			ratio = 300;
			delta = 1;

			ntime = System.currentTimeMillis();

			if (this.otim[opos] == 0L) {
				ratio = lastRatio;
				delta = lastDelta;
			} else if (ntime > this.otim[opos]) {
				ratio = (int) ((long) (this.deltime * 2560) / (ntime - this.otim[opos]));
			}

			if (ratio < 25) {
				ratio = 25;
			}

			if (ratio > 256) {
				ratio = 256;
				delta = (int) ((long) this.deltime - (ntime - this.otim[opos]) / 10L);
			}

			if (delta > this.deltime) {
				delta = this.deltime;
			}

			this.otim[opos] = ntime;
			opos = (opos + 1) % 10;

			if (delta > 1) {
				for (int i = 0; i < 10; i++) {
					if (this.otim[i] != 0L) {
						this.otim[i] += delta;
					}
				}
			}

			if (delta < this.mindel) {
				delta = this.mindel;
			}

			try {
				Thread.sleep((long) delta);
			} catch (InterruptedException ignore) {
				intex++;
			}

			while (count < 256) {
				this.mouseClickButton = this.nextMouseClickButton;
				this.mouseClickX = this.nextMouseClickX;
				this.mouseClickY = this.nextMouseClickY;
				this.mouseClickTime = this.nextMouseClickTime;
				this.nextMouseClickButton = 0;

				this.update();

				this.keyQueueReadPos = this.keyQueueWritePos;
				count += ratio;
			}

			count &= 0xFF;

			if (this.deltime > 0) {
				this.fps = ratio * 1000 / (this.deltime * 256);
			}

			this.draw();

			if (this.debug) {
				System.out.println("ntime:" + ntime);
				for (int i = 0; i < 10; i++) {
					int o = (opos - i - 1 + 20) % 10;
					System.out.println("otim" + o + ":" + this.otim[o]);
				}
				System.out.println("fps:" + this.fps + " ratio:" + ratio + " count:" + count);
				System.out.println("del:" + delta + " deltime:" + this.deltime + " mindel:" + this.mindel);
				System.out.println("intex:" + intex + " opos:" + opos);
				this.debug = false;
				intex = 0;
			}
		}

		if (this.state == -1) {
			this.shutdown();
		}
	}

	@ObfuscatedName("JWWAIQPI.a(Z)V")
	public void shutdown() {
		this.state = -2;
		this.unload();

		if (this.frame == null) {
			return;
		}

		try {
			Thread.sleep(1000L);
		} catch (Exception ignore) {
		}

		try {
			System.exit(0);
		} catch (Throwable ignore) {
		}
	}

	@ObfuscatedName("JWWAIQPI.a(BI)V")
	public void setFramerate(int fps) {
		this.deltime = 1000 / fps;
	}

	@ObfuscatedName("JWWAIQPI.a(I)I")
	public int pollKey() {
		int key = -1;
		if (this.keyQueueWritePos != this.keyQueueReadPos) {
			key = this.keyQueue[this.keyQueueReadPos];
			this.keyQueueReadPos = this.keyQueueReadPos + 1 & 0x7F;
		}
		return key;
	}

	@ObfuscatedName("JWWAIQPI.a()V")
	public void load() {
	}

	@ObfuscatedName("JWWAIQPI.a(B)V")
	public void update() {
	}

	@ObfuscatedName("JWWAIQPI.b(I)V")
	public void unload() {
	}

	@ObfuscatedName("JWWAIQPI.c(I)V")
	public void draw() {
	}

	@ObfuscatedName("JWWAIQPI.b(B)V")
	public void refresh() {
	}

	@ObfuscatedName("JWWAIQPI.d(I)Ljava/awt/Component;")
	public PlatformContext getBaseComponent() {
		return this;
	}

	@ObfuscatedName("JWWAIQPI.a(Ljava/lang/Runnable;I)V")
	public void startThread(Runnable thread, int priority) {
		Thread t = new Thread(thread);
		t.start();
		t.setPriority(priority);
	}

	@ObfuscatedName("JWWAIQPI.a(IZLjava/lang/String;)V")
	public void drawProgress(int percent, @NotNull String message) {}
}
