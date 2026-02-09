package meteor.context.platform.vanilla

import jagex2.graphics.PixMap
import meteor.context.events.VanillaPixMapDraw
import util.GlobalEventBus
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.awt.image.DirectColorModel
import java.awt.image.Raster

class VanillaPixMap(width: Int, height: Int) : PixMap(width, height) {
    val colorModel = DirectColorModel(32, 16711680, 65280, 255)
    var image: Image? = null

    init {
        val buffer = DataBufferInt(data, data.size)
        val raster = Raster.createWritableRaster(colorModel.createCompatibleSampleModel(width, height), buffer, null)
        image = BufferedImage(colorModel, raster, false, null)
        setPixels()
    }

    override fun draw(y: Int, x: Int) {
        GlobalEventBus.publish(VanillaPixMapDraw(image, x, y))
    }
}