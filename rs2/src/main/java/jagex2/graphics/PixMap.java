package jagex2.graphics;

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;

import java.awt.*;
import java.awt.image.*;
import java.util.Hashtable;

// name taken from rsc
@OriginalClass("client.client!qb")
public class PixMap implements ImageProducer, ImageObserver {

	@OriginalMember(owner = "client.client!qb", name = "b", descriptor = "[I")
	public final int[] pixels;

	@OriginalMember(owner = "client.client!qb", name = "c", descriptor = "I")
	public final int width;

	@OriginalMember(owner = "client.client!qb", name = "d", descriptor = "I")
	public final int height;

	@OriginalMember(owner = "client.client!qb", name = "e", descriptor = "Ljava/awt/image/ColorModel;")
	private final ColorModel colorModel;

	@OriginalMember(owner = "client.client!qb", name = "f", descriptor = "Ljava/awt/image/ImageConsumer;")
	private ImageConsumer imageConsumer;

	@OriginalMember(owner = "client.client!qb", name = "g", descriptor = "Ljava/awt/Image;")
	private final Image image;

	@OriginalMember(owner = "client.client!qb", name = "<init>", descriptor = "(Ljava/awt/Component;III)V")
	public PixMap(@OriginalArg(0) Component c, @OriginalArg(1) int width, @OriginalArg(3) int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
		DataBufferInt buffer = new DataBufferInt(pixels, pixels.length);
		this.colorModel = new DirectColorModel(32, 0xff0000, 0xff00, 0xff);
		WritableRaster raster = Raster.createWritableRaster(colorModel.createCompatibleSampleModel(width, height), buffer, null);
		this.image = new BufferedImage(colorModel, raster, false, new Hashtable<>());
		this.bind();
	}

	@OriginalMember(owner = "client.client!qb", name = "a", descriptor = "(B)V")
	public void bind() {
		Pix2D.bind(this.width, this.height, this.pixels);
	}

	@OriginalMember(owner = "client.client!qb", name = "a", descriptor = "(ILjava/awt/Graphics;II)V")
	public void draw(@OriginalArg(1) Graphics g, @OriginalArg(2) int width, @OriginalArg(0) int height) {
		this.setPixels();
		g.drawImage(this.image, width, height, this);
	}

	@OriginalMember(owner = "client.client!qb", name = "addConsumer", descriptor = "(Ljava/awt/image/ImageConsumer;)V")
	@Override
	public synchronized void addConsumer(@OriginalArg(0) ImageConsumer consumer) {
		this.imageConsumer = consumer;
		consumer.setDimensions(this.width, this.height);
		consumer.setProperties(null);
		consumer.setColorModel(this.colorModel);
		consumer.setHints(14);
	}

	@OriginalMember(owner = "client.client!qb", name = "isConsumer", descriptor = "(Ljava/awt/image/ImageConsumer;)Z")
	@Override
	public synchronized boolean isConsumer(@OriginalArg(0) ImageConsumer consumer) {
		return this.imageConsumer == consumer;
	}

	@OriginalMember(owner = "client.client!qb", name = "removeConsumer", descriptor = "(Ljava/awt/image/ImageConsumer;)V")
	@Override
	public synchronized void removeConsumer(@OriginalArg(0) ImageConsumer consumer) {
		if (this.imageConsumer == consumer) {
			this.imageConsumer = null;
		}
	}

	@OriginalMember(owner = "client.client!qb", name = "startProduction", descriptor = "(Ljava/awt/image/ImageConsumer;)V")
	@Override
	public void startProduction(@OriginalArg(0) ImageConsumer consumer) {
		this.addConsumer(consumer);
	}

	@OriginalMember(owner = "client.client!qb", name = "requestTopDownLeftRightResend", descriptor = "(Ljava/awt/image/ImageConsumer;)V")
	@Override
	public void requestTopDownLeftRightResend(@OriginalArg(0) ImageConsumer consumer) {
		System.out.println("TDLR");
	}

	@OriginalMember(owner = "client.client!qb", name = "a", descriptor = "()V")
    public synchronized void setPixels() {
		if (this.imageConsumer != null) {
			this.imageConsumer.setPixels(0, 0, this.width, this.height, this.colorModel, this.pixels, 0, this.width);
			this.imageConsumer.imageComplete(2);
		}
	}

	@OriginalMember(owner = "client.client!qb", name = "imageUpdate", descriptor = "(Ljava/awt/Image;IIIII)Z")
	@Override
	public boolean imageUpdate(@OriginalArg(0) Image img, @OriginalArg(1) int infoflags, @OriginalArg(2) int x, @OriginalArg(3) int y, @OriginalArg(4) int width, @OriginalArg(5) int height) {
		return true;
	}
}
