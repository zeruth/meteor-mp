package meteor.platform

import jagex3.client.input.mouse.ClientMouseWheelListener
import jagex3.graphics.Pix32
import jagex3.graphics.PixMap

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
    fun update()
    fun paint()
    fun repaint()
    fun drawProgress(progress: Int, message: String)
    fun resetProgress()
}