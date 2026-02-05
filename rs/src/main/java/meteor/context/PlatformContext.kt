package meteor.context

import jagex2.client.ViewBox
import jagex2.graphics.Pix32
import jagex2.graphics.PixMap
import java.awt.Font
import java.awt.FontMetrics
import java.net.URL
import kotlin.invoke
import kotlin.reflect.KClass

open class PlatformContext{
    open lateinit var viewBoxImplementation: KClass<out ViewBox>
    open lateinit var pixMapImplementation: KClass<out PixMap>
    open lateinit var getCacheDirImpl : (() -> String)

    fun getCacheDir(): String = getCacheDirImpl.invoke()

    fun showDocument(url: URL) {}

    open fun drawError() {}

    fun getFontMetrics(font: Font): FontMetrics = throw Exception()

    open fun getCodeBase() : URL? = null

    fun getDocumentBase() : URL? = null

    open fun getParameter(key: String) : String? = null

    open fun createPix32(src: ByteArray): Pix32? = null

    fun createViewBox(width: Int, height: Int): ViewBox {
        return viewBoxImplementation.create(width, height)
    }

    fun createPixmap(width: Int, height: Int): PixMap {
        return pixMapImplementation.create(width, height)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> KClass<*>.create(vararg args: Any) : T {
        return constructors.first().call(*args) as T
    }
}