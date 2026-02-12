package meteor.platform.android

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import jagex3.client.GameShell
import jagex3.client.input.mouse.ClientMouseWheelListener
import jagex3.graphics.Pix32
import jagex3.graphics.PixMap
import jagex3.sound.JavaPcmPlayer
import meteor.events.AndroidPixMapDraw
import meteor.events.Draw
import meteor.platform.Context
import meteor.platform.ContextCommon
import util.GlobalEventBus
import java.net.URL

class AndroidContext(val context: android.content.Context) : Context {

    val bitmap = createBitmap(765, 503, Bitmap.Config.RGB_565)
    private val canvas = Canvas(bitmap)

    init {
        GlobalEventBus.subscribe<AndroidPixMapDraw> {
            if (it.payload.w == null) {
                canvas.drawBitmap(
                    it.payload.image!!,
                    it.payload.x.toFloat(),
                    it.payload.y.toFloat(),
                    null
                )
            }
            else {
                canvas.clipRect(
                    it.payload.x,
                    it.payload.y,
                    it.payload.x + it.payload.w!!,
                    it.payload.y + it.payload.h!!
                )

                canvas.drawBitmap(
                    (GameShell.context as AndroidContext).bitmap,
                    0f,
                    0f,
                    null
                )
            }
        }
    }

    override fun startApplication(width: Int, height: Int) {}
    override fun addcanvas() {}
    override fun mainredrawwrapper() {}
    override fun shutdown() {}
    override fun addKeyListeners() {}
    override fun removeKeyListeners() {}
    override fun addMouseListeners() {}
    override fun removeMouseListeners() {}
    override fun addMouseWheelListener(iface: ClientMouseWheelListener) {}
    override fun removeMouseWheelListener(iface: ClientMouseWheelListener) {}
    override fun repaintCanvas() {}
    override fun repaint() {}
    override fun drawProgress(progress: Int, message: String) {
        println(message)
    }
    override fun resetProgress() {}
    override fun showDocument(url: URL, sub: String) {}
    override fun keepThreadAlive() = true

    override fun getCodeBase(): URL {
        return ContextCommon.getCodeBase()
    }

    override fun getParameter(name: String): String? {
        return ContextCommon.getParameter(name)
    }

    override fun getCacheDirectory(): String {
        return context.dataDir.absolutePath
    }

    override fun createPcmPlayer(): JavaPcmPlayer {
        return AndroidPcmPlayer()
    }

    override fun onDraw() {

    }

    override fun createPixMap(width: Int, height: Int): PixMap {
        return AndroidPixMap(width, height).apply {
            create(width, height)
        }
    }

    override fun createPix32(data: ByteArray) : Pix32 {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
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
            this.data = pixels
        }
    }
}