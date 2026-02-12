package meteor.platform.android

import android.graphics.BitmapFactory
import jagex2.client.Client
import jagex2.client.ViewBox
import jagex2.graphics.Pix32
import jagex2.graphics.PixMap
import meteor.platform.Context

open class AndroidContext : Context() {
    override fun createPixmap(width: Int, height: Int): PixMap {
        return AndroidPixMap(width, height)
    }

    override fun createViewBox(width: Int, height: Int): ViewBox {
        return AndroidViewBox(width, height)
    }

    override var getCacheDirImpl: () -> String = {
        var cacheDir = ""
        Client.context!!.dataDir!!.resolve("meteor-377/cache/").let {
            if (!it.exists())
                it.mkdirs()
            cacheDir = it.absolutePath
        }
        cacheDir
    }

    override fun createPix32(src: ByteArray): Pix32 {
        val bitmap = BitmapFactory.decodeByteArray(src, 0, src.size)
            ?: throw IllegalArgumentException("Invalid image data")

        val wi = bitmap.width
        val hi = bitmap.height

        val pixels = IntArray(wi * hi)
        bitmap.getPixels(pixels, 0, wi, 0, 0, wi, hi)

        return Pix32().apply {
            this.wi = wi
            this.hi = hi
            this.owi = wi
            this.ohi = hi
            this.xof = 0
            this.yof = 0
            this.pixels = pixels
        }
    }
}



