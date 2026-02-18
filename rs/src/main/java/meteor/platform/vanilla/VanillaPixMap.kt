package meteor.platform.vanilla

import jagex3.graphics.PixMap
import java.awt.Image
import java.awt.Shape
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.awt.image.DirectColorModel
import java.awt.image.Raster
import java.util.*

class VanillaPixMap(val context: VanillaContext, width: Int, height: Int) : PixMap(width, height) {
    var image: Image

    init {
        val var4 = DataBufferInt(this.data, this.data.size)
        val var5 = DirectColorModel(32, 0xff0000, 0xff00, 0xff)
        val var6 = Raster.createWritableRaster(var5.createCompatibleSampleModel(this.width, this.height), var4, null)
        this.image = BufferedImage(var5, var6, false, Hashtable<Any?, Any?>())

        this.bind()
    }

    override fun draw(x: Int, y: Int) {
        context.graphics().drawImage(this.image, x, y, context.canvas)
    }

    override fun draw(x: Int, y: Int, w: Int, h: Int) {
        val g = context.graphics()
        val save: Shape? = g.clip
        g.clipRect(x, y, w, h)
        g.drawImage(this.image, 0, 0, context.canvas)
        g.clip = save
    }
}