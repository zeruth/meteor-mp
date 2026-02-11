package meteor.events

import android.graphics.Bitmap

class AndroidPixMapDraw(
    @JvmField var image: Bitmap?,
    @JvmField var x: Int,
    @JvmField var y: Int,
    @JvmField var w: Int? = null,
    @JvmField var h: Int? = null,
)