package meteor.events

import java.awt.image.BufferedImage

class DesktopPixMapDraw(
    @JvmField var image: BufferedImage?,
    @JvmField var x: Int,
    @JvmField var y: Int,
    @JvmField var w: Int? = null,
    @JvmField var h: Int? = null,
)