package jagex2.client;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.*;
import java.awt.image.BufferedImage;

// name taken from rsc
@OriginalClass("client!b")
public class ViewBox extends BufferedImage {

	@OriginalMember(owner = "client!b", name = "b", descriptor = "Lclient!a;")
	private final GameShell shell;

	public Insets insets;

	public Graphics graphics;

	@OriginalMember(owner = "client!b", name = "<init>", descriptor = "(IILclient!a;I)V")
	public ViewBox(@OriginalArg(2) GameShell shell, @OriginalArg(3) int width, @OriginalArg(0) int height) {
		super(width, height, TYPE_INT_RGB);
		this.shell = shell;
		graphics = createGraphics();
		GameShell.graphics = graphics;
/*		this.shell = shell;
		this.setTitle("Jagex");
		this.setResizable(false);
		this.show();
		this.toFront();
		this.insets = this.getInsets();
		this.resize(width + this.insets.left + this.insets.bottom, height + this.insets.top + this.insets.bottom);*/
	}

	@OriginalMember(owner = "client!b", name = "getGraphics", descriptor = "()Ljava/awt/Graphics;")
	@Override
	public Graphics getGraphics() {
		return graphics;
	}

/*	@OriginalMember(owner = "client!b", name = "update", descriptor = "(Ljava/awt/Graphics;)V")
	public void update(@OriginalArg(0) Graphics g) {
		this.shell.update(g);
	}*/

	@OriginalMember(owner = "client!b", name = "paint", descriptor = "(Ljava/awt/Graphics;)V")
	public void paint(@OriginalArg(0) Graphics g) {
		this.shell.paint(g);
	}
}
