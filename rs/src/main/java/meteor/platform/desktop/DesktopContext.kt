package meteor.platform.desktop

import jagex3.client.input.mouse.ClientMouseWheelListener
import jagex3.graphics.Pix32
import jagex3.graphics.PixMap
import jagex3.sound.JavaPcmPlayer
import meteor.events.DesktopPixMapDraw
import meteor.platform.Context
import meteor.platform.ContextCommon
import meteor.platform.vanilla.VanillaPcmPlayer
import meteor.platform.vanilla.VanillaPix32
import util.GlobalEventBus
import java.awt.image.BufferedImage
import java.net.URL

class DesktopContext : Context {

    val image = BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB)

    init {
        GlobalEventBus.subscribe<DesktopPixMapDraw> {
            if (it.payload.w == null) {
                image.graphics.drawImage(it.payload.image, it.payload.x, it.payload.y, null)
            }
            else {
                image.graphics.drawImage(it.payload.image, it.payload.x, it.payload.y, it.payload.w!!, it.payload.h!!, null)
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
        return System.getProperty("user.home") + "/.meteor-os1"
    }

    override fun createPcmPlayer(): JavaPcmPlayer {
        return VanillaPcmPlayer()
    }

    override fun onDraw() {

    }

    override fun createPixMap(width: Int, height: Int): PixMap {
        return DesktopPixMap(width, height)
    }

    override fun createPix32(data: ByteArray): Pix32 {
        return VanillaPix32(data, this)
    }

    override fun getCanvas(): Any? = null
}