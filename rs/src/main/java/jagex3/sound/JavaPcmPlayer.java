package jagex3.sound;

import deob.ObfuscatedName;

@ObfuscatedName("ds")
public class JavaPcmPlayer extends PcmPlayer {

	@ObfuscatedName("ds.as")
	public int lineCapacity;

	@ObfuscatedName("ds.am")
	public byte[] buffer;

	@ObfuscatedName("ds.s(Ljava/awt/Component;)V")
	public void init() {
		this.buffer = new byte[0x100 << (PcmPlayer.stereo ? 2 : 1)];
	}

	@ObfuscatedName("ds.u(I)V")
	public void open(int arg0) {
		this.lineCapacity = arg0;
	}

	@ObfuscatedName("ds.v()I")
	public int queued() {
		return this.lineCapacity - (available() >> (PcmPlayer.stereo ? 2 : 1));
	}

	public int available() {
		return -1;
	}

	@ObfuscatedName("ds.w()V")
	public int write(int current, int goal) {
		int sizeBytes = 256;
		if (PcmPlayer.stereo) {
			sizeBytes <<= 0x1;
		}
		for (int var2 = 0; var2 < sizeBytes; var2++) {
			int var3 = this.samples[var2];
			if ((var3 + 0x800000 & 0xFF000000) != 0) {
				var3 = var3 >> 31 ^ 0x7FFFFF;
			}
			this.buffer[var2 * 2] = (byte) (var3 >> 8);
			this.buffer[var2 * 2 + 1] = (byte) (var3 >> 16);
		}
		return sizeBytes;
	}

	@ObfuscatedName("ds.e()V")
	public void close() {}

	@ObfuscatedName("ds.b()V")
	public void flush() {}
}
