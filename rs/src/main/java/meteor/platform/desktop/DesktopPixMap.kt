package meteor.platform.desktop

import jagex3.graphics.PixMap
import meteor.events.DesktopPixMapDraw
import util.GlobalEventBus
import java.awt.image.BufferedImage

class DesktopPixMap(width: Int, height: Int) : PixMap(width, height) {

    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    init {
        bind()
    }

    fun update() {
        image.setRGB(
            0, 0,
            width, height,
            data,
            0,
            width
        )
    }

    override fun draw(x: Int, y: Int) {
        update()
        GlobalEventBus.publish(DesktopPixMapDraw(image, x, y))
    }

    override fun draw(x: Int, y: Int, w: Int, h: Int) {
        update()
        GlobalEventBus.publish(DesktopPixMapDraw(image, x, y, w, h))
    }
}
