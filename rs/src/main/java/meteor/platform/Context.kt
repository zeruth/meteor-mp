package meteor.platform

import jagex3.client.input.mouse.ClientMouseWheelListener
import jagex3.graphics.Pix32
import jagex3.graphics.PixMap
import jagex3.sound.JavaPcmPlayer
import java.net.URL

interface Context {
    fun startApplication(width: Int, height: Int)
    fun addcanvas()
    fun mainredrawwrapper()
    fun shutdown()
    fun addKeyListeners()
    fun removeKeyListeners()
    fun addMouseListeners()
    fun removeMouseListeners()
    fun addMouseWheelListener(iface: ClientMouseWheelListener)
    fun removeMouseWheelListener(iface: ClientMouseWheelListener)
    fun createPixMap(width: Int, height: Int) : PixMap
    fun createPix32(data: ByteArray) : Pix32
    fun repaintCanvas()
    fun repaint()
    fun drawProgress(progress: Int, message: String)
    fun resetProgress()
    fun showDocument(url: URL, sub: String)
    fun keepThreadAlive(): Boolean

    fun getCodeBase(): URL

    fun getParameter(name: String): String?
    fun getCacheDirectory(): String
    fun createPcmPlayer() : JavaPcmPlayer
    fun onDraw()

    fun getCanvas(): Any? {
        return null
    }
}