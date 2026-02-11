package meteor.platform

import jagex3.client.input.mouse.ClientMouseWheelListener

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
}