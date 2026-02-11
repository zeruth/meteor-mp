package meteor.platform.headless

import jagex3.client.input.mouse.ClientMouseWheelListener
import jagex3.graphics.Pix32
import jagex3.graphics.PixMap
import meteor.platform.Context

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
    override fun update() {}
    override fun paint() {}
    override fun repaint() {}
    override fun drawProgress(progress: Int, message: String) {}
    override fun resetProgress() {}

    override fun createPixMap(width: Int, height: Int): PixMap {
        TODO("Not yet implemented")
    }

    override fun createPix32(data: ByteArray) = Pix32()
}