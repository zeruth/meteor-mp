package meteor.platform.android

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import jagex3.graphics.PixMap
import meteor.events.AndroidPixMapDraw
import util.GlobalEventBus

class AndroidPixMap(width: Int, height: Int) : PixMap(width, height) {
    val bitmap: Bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

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

    override fun draw(x: Int, y: Int) {
        update()
        GlobalEventBus.publish(AndroidPixMapDraw(bitmap, x, y))
    }

    override fun draw(x: Int, y: Int, w: Int, h: Int) {
        update()
        GlobalEventBus.publish(AndroidPixMapDraw(bitmap, x, y, w, h))
    }

    override fun create(w: Int, h: Int) {
        bind()
    }
}