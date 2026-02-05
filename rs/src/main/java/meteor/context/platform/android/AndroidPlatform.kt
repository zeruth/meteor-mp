package meteor.context.platform.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import jagex2.client.Client
import jagex2.client.GameShell
import jagex2.client.ViewBox
import jagex2.graphics.Pix32
import jagex2.graphics.PixMap
import meteor.context.PlatformContext
import meteor.context.events.AndroidPixMapDraw
import util.GlobalEventBus
import kotlin.reflect.KClass

open class AndroidPlatform : PlatformContext() {
    override var viewBoxImplementation: KClass<out ViewBox> = AndroidViewBox::class
    override var pixMapImplementation: KClass<out PixMap> = AndroidPixMap::class
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

class AndroidViewBox(width: Int, height: Int) : ViewBox(Client.client, width, height) {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    private val canvas = Canvas(bitmap)

    init {
        GlobalEventBus.subscribe<AndroidPixMapDraw> {
            val e = it.payload
            canvas.drawBitmap(
                e.image!!,
                e.x.toFloat(),
                e.y.toFloat(),
                null
            )
        }
    }
}

class AndroidPixMap(width: Int, height: Int) : PixMap(width, height) {
    val bitmap: Bitmap =
        Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

    init {
        setPixels()
    }

    fun update() {
        bitmap.setPixels(
            data,
            0,
            width,
            0, 0,
            width,
            height
        )
    }

    override fun draw(y: Int, x: Int) {
        update()
        GlobalEventBus.publish(
            AndroidPixMapDraw(bitmap, x, y)
        )
    }
}