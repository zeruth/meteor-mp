package meteor.platform.android

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import jagex2.client.Client
import jagex2.client.ViewBox
import meteor.events.AndroidPixMapDraw
import util.GlobalEventBus

class AndroidViewBox(width: Int, height: Int) : ViewBox(Client.client, width, height) {
    val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)
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