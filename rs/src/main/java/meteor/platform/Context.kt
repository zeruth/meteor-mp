package meteor.platform

interface Context {
    fun startApplication(width: Int, height: Int)
    fun addcanvas()
    fun mainredrawwrapper()
    fun shutdown()
}