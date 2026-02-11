package meteor.platform.headless

import jagex3.client.input.mouse.ClientMouseWheelListener
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
}