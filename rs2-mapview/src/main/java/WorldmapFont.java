import jagex2.client.GameShellMapView;
import jagex2.graphics.Pix2DMapView;

import java.awt.*;
import java.awt.image.PixelGrabber;

public final class WorldmapFont extends Pix2DMapView {

	private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"£$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";

	private static int[] fontChar = new int[256];

	private boolean fontCharFlagged = false;

	private int fontCharPos = 0;

	private byte[] fontCharInfo = new byte[100000];

	static {
		for ( int i = 0; i < 256; i++) {
			int c = CHARSET.indexOf(i);
			if (c == -1) {
				c = 'J';
			}
			fontChar[i] = c * 9;
		}
	}

	public WorldmapFont( int size, boolean bold, mapview shell) {
		this.fontCharPos = 855;
		this.fontCharFlagged = false;

		Font font = new Font("Helvetica", bold ? Font.BOLD : Font.PLAIN, size);
		FontMetrics metrics = shell.getFontMetrics(font);

		for (int i = 0; i < 95; i++) {
			this.load(font, metrics, CHARSET.charAt(i), i, false, shell);
		}

		if (bold && this.fontCharFlagged) {
			this.fontCharPos = 855;
			this.fontCharFlagged = false;

			font = new Font("Helvetica", Font.PLAIN, size);
			metrics = shell.getFontMetrics(font);

			for (int i = 0; i < 95; i++) {
				this.load(font, metrics, CHARSET.charAt(i), i, false, shell);
			}

			if (!this.fontCharFlagged) {
				this.fontCharPos = 855;
				this.fontCharFlagged = false;

				for (int i = 0; i < 95; i++) {
					this.load(font, metrics, CHARSET.charAt(i), i, true, shell);
				}
			}
		}

		byte[] chars = new byte[this.fontCharPos];
		for ( int i = 0; i < this.fontCharPos; i++) {
			chars[i] = this.fontCharInfo[i];
		}
		this.fontCharInfo = chars;
	}

	public static BufferedFrame frame = null;

	private void load( Font font, FontMetrics metrics, char c, int id, boolean offset, mapview shell) {
		int width = metrics.charWidth(c);
		int initialWidth = width;

		if (offset) {
			try {
				if (c == '/') {
					offset = false;
				}

				if (c == 'f' || c == 't' || c == 'w' || c == 'v' || c == 'k' || c == 'x' || c == 'y' || c == 'A' || c == 'V' || c == 'W') {
					width++;
				}
			} catch ( Exception ignore) {
			}
		}

		int maxAscent = metrics.getMaxAscent();
		int totalDescent = metrics.getMaxAscent() + metrics.getMaxDescent();
		int height = metrics.getHeight();

		if (frame == null)
			frame = new BufferedFrame(shell);

		Graphics g = GameShellMapView.image.getGraphics();
		Image image = GameShellMapView.image;
		g.setColor(Color.black);
		g.fillRect(0, 0, width, totalDescent);

		g.setColor(Color.white);
		g.setFont(font);
		g.drawString(c + "", 0, maxAscent);

		if (offset) {
			g.drawString(c + "", 1, maxAscent);
		}

		int[] pixels = new int[width * totalDescent];
		PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, totalDescent, pixels, 0, width);
		try {
			grabber.grabPixels();
		} catch ( Exception ignore) {
		}
		image.flush();
		image = null;

		int left = 0;
		int top = 0;
		int right = width;
		int bottom = totalDescent;

		findTop: for (int y = 0; y < totalDescent; y++) {
			for (int x = 0; x < width; x++) {
				int color = pixels[x + y * width];
				if ((color & 0xFFFFFF) != 0) {
					top = y;
					break findTop;
				}
			}
		}

		findLeft: for (int x = 0; x < width; x++) {
			for (int y = 0; y < totalDescent; y++) {
				int color = pixels[x + y * width];
				if ((color & 0xFFFFFF) != 0) {
					left = x;
					break findLeft;
				}
			}
		}

		findBottom: for (int y = totalDescent - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				int color = pixels[x + y * width];
				if ((color & 0xFFFFFF) != 0) {
					bottom = y + 1;
					break findBottom;
				}
			}
		}

		findRight: for (int x = width - 1; x >= 0; x--) {
			for (int y = 0; y < totalDescent; y++) {
				int color = pixels[x + y * width];
				if ((color & 0xFFFFFF) != 0) {
					right = x + 1;
					break findRight;
				}
			}
		}

		this.fontCharInfo[id * 9] = (byte) (this.fontCharPos / 0x4000);
		this.fontCharInfo[id * 9 + 1] = (byte) (this.fontCharPos / 0x80 & 0x7F);
		this.fontCharInfo[id * 9 + 2] = (byte) (this.fontCharPos & 0x7F);
		this.fontCharInfo[id * 9 + 3] = (byte) (right - left);
		this.fontCharInfo[id * 9 + 4] = (byte) (bottom - top);
		this.fontCharInfo[id * 9 + 5] = (byte) left;
		this.fontCharInfo[id * 9 + 6] = (byte) (maxAscent - top);
		this.fontCharInfo[id * 9 + 7] = (byte) initialWidth;
		this.fontCharInfo[id * 9 + 8] = (byte) height;

		for (int y = top; y < bottom; y++) {
			for (int x = left; x < right; x++) {
				int color = pixels[x + y * width] & 0xFF;

				if (color > 30 && color < 230) {
					this.fontCharFlagged = true;
				}

				this.fontCharInfo[this.fontCharPos++] = (byte) color;
			}
		}
	}

	private void drawString( String str, int x, int y, int rgb, boolean shadowed) {
		try {
			if (this.fontCharFlagged || rgb == 0) {
				shadowed = false;
			}

			for ( int i = 0; i < str.length(); i++) {
				int c = fontChar[str.charAt(i)];

				if (shadowed) {
					this.drawChar(c, x + 1, y, 0, this.fontCharInfo, this.fontCharFlagged);
					this.drawChar(c, x, y + 1, 0, this.fontCharInfo, this.fontCharFlagged);
				}

				this.drawChar(c, x, y, rgb, this.fontCharInfo, this.fontCharFlagged);
				x += this.fontCharInfo[c + 7];
			}
		} catch ( Exception ex) {
			System.out.println("drawstring: " + ex);
			ex.printStackTrace();
		}
	}

	public void drawStringCenter( String str, int x, int y, int rgb, boolean shadowed) {
		int xOffset = this.stringWidth(str) / 2;
		int yOffset = this.getYOffset();
		if (x - xOffset <= Pix2DMapView.boundRight && (x + xOffset >= Pix2DMapView.boundLeft && (y - yOffset <= Pix2DMapView.boundBottom && y >= 0))) {
			this.drawString(str, x - xOffset, y, rgb, shadowed);
		}
	}

	private void plotLetter2( int[] arg0, byte[] arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
		for ( int local4 = -arg6; local4 < 0; local4++) {
			for ( int local9 = -arg5; local9 < 0; local9++) {
				int local18 = arg1[arg3++] & 0xFF;
				if (local18 <= 30) {
					arg4++;
				} else if (local18 >= 230) {
					arg0[arg4++] = arg2;
				} else {
					int local35 = arg0[arg4];
					arg0[arg4++] = ((arg2 & 0xFF00FF) * local18 + (local35 & 0xFF00FF) * (256 - local18) & 0xFF00FF00) + ((arg2 & 0xFF00) * local18 + (local35 & 0xFF00) * (256 - local18) & 0xFF0000) >> 8;
				}
			}
			arg4 += arg7;
			arg3 += arg8;
		}
	}

	public int getHeight() {
		return this.fontCharInfo[8] - 1; // (byte) height;
	}

	private int stringWidth( String arg0) {
		int local3 = 0;
		for ( int local5 = 0; local5 < arg0.length(); local5++) {
			if (arg0.charAt(local5) == '@' && local5 + 4 < arg0.length() && arg0.charAt(local5 + 4) == '@') {
				local5 += 4;
			} else if (arg0.charAt(local5) == '~' && local5 + 4 < arg0.length() && arg0.charAt(local5 + 4) == '~') {
				local5 += 4;
			} else {
				local3 += this.fontCharInfo[fontChar[arg0.charAt(local5)] + 7];
			}
		}
		return local3;
	}

	private void drawChar( int c, int x, int y, int rgb, byte[] info, boolean flagged) {
		int local7 = x + info[c + 5];
		int local15 = y - info[c + 6];
		int local21 = info[c + 3];
		int local27 = info[c + 4];
		int local47 = info[c] * 16384 + info[c + 1] * 128 + info[c + 2];
		int local53 = local7 + local15 * Pix2DMapView.width2d;
		int local57 = Pix2DMapView.width2d - local21;
		int local59 = 0;
		int local66;
		if (local15 < Pix2DMapView.boundTop) {
			local66 = Pix2DMapView.boundTop - local15;
			local27 -= local66;
			local15 = Pix2DMapView.boundTop;
			local47 += local66 * local21;
			local53 += local66 * Pix2DMapView.width2d;
		}
		if (local15 + local27 >= Pix2DMapView.boundBottom) {
			local27 -= local15 + local27 - Pix2DMapView.boundBottom + 1;
		}
		if (local7 < Pix2DMapView.boundLeft) {
			local66 = Pix2DMapView.boundLeft - local7;
			local21 -= local66;
			local7 = Pix2DMapView.boundLeft;
			local47 += local66;
			local53 += local66;
			local59 += local66;
			local57 += local66;
		}
		if (local7 + local21 >= Pix2DMapView.boundRight) {
			local66 = local7 + local21 - Pix2DMapView.boundRight + 1;
			local21 -= local66;
			local59 += local66;
			local57 += local66;
		}
		if (local21 <= 0 || local27 <= 0) {
			return;
		}
		if (flagged) {
			this.plotLetter2(Pix2DMapView.data, info, rgb, local47, local53, local21, local27, local57, local59);
		} else {
			this.plotLetter(Pix2DMapView.data, info, rgb, local47, local53, local21, local27, local57, local59);
		}
	}

	private void plotLetter( int[] arg0, byte[] arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
		try {
			int local6 = -(arg5 >> 2);
			int local11 = -(arg5 & 0x3);
			for ( int local14 = -arg6; local14 < 0; local14++) {
				int local18;
				for (local18 = local6; local18 < 0; local18++) {
					if (arg1[arg3++] == 0) {
						arg4++;
					} else {
						arg0[arg4++] = arg2;
					}
					if (arg1[arg3++] == 0) {
						arg4++;
					} else {
						arg0[arg4++] = arg2;
					}
					if (arg1[arg3++] == 0) {
						arg4++;
					} else {
						arg0[arg4++] = arg2;
					}
					if (arg1[arg3++] == 0) {
						arg4++;
					} else {
						arg0[arg4++] = arg2;
					}
				}
				for (local18 = local11; local18 < 0; local18++) {
					if (arg1[arg3++] == 0) {
						arg4++;
					} else {
						arg0[arg4++] = arg2;
					}
				}
				arg4 += arg7;
				arg3 += arg8;
			}
		} catch ( Exception ex) {
			System.out.println("plotletter: " + ex);
			ex.printStackTrace();
		}
	}

	public int getYOffset() {
		return this.fontCharInfo[6]; // (byte) (maxAscent - top);
	}
}
