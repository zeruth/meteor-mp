package meteor.platform.headless

import jagex3.client.input.mouse.ClientMouseWheelListener
import jagex3.graphics.Pix32
import jagex3.graphics.PixMap
import jagex3.sound.JavaPcmPlayer
import meteor.platform.Context
import meteor.platform.ContextCommon
import java.net.URL

class HeadlessContext : Context {
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
    override fun drawProgress(progress: Int, message: String) {}
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
        return ""
    }

    override fun createPcmPlayer(): JavaPcmPlayer {
        return JavaPcmPlayer()
    }

    override fun onDraw() {

    }

    override fun createPixMap(width: Int, height: Int): PixMap {
        return object : PixMap(width, height) {
            override fun draw(x: Int, y: Int) {}
            override fun draw(x: Int, y: Int, w: Int, h: Int) {}
            override fun create(w: Int, h: Int) {}
        }
    }

    override fun createPix32(data: ByteArray) = Pix32()
}