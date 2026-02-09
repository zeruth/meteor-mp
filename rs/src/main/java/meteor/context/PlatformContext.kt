package meteor.context

import jagex2.client.ViewBox
import jagex2.graphics.Pix32
import jagex2.graphics.PixMap
import java.awt.Font
import java.awt.FontMetrics
import java.net.URL
import kotlin.reflect.KClass

open class PlatformContext{
    open lateinit var viewBoxImplementation: KClass<out ViewBox>
    open lateinit var pixMapImplementation: KClass<out PixMap>
    open lateinit var getCacheDirImpl : (() -> String)

    fun getCacheDir(): String = getCacheDirImpl.invoke()

    open fun showDocument(url: URL) { throw Exception() }

    open fun drawError() { throw Exception() }

    open fun getFontMetrics(font: Font): FontMetrics = throw Exception()

    open fun getCodeBase() : URL? = throw Exception()

    open fun getParameter(key: String) : String? = throw Exception()

    open fun createPix32(src: ByteArray): Pix32? = throw Exception()

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