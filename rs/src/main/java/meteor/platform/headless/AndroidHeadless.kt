package meteor.platform.headless

import jagex2.client.Client
import jagex2.client.ViewBox
import jagex2.graphics.Pix32
import jagex2.graphics.PixMap
import meteor.platform.Context
import kotlin.reflect.KClass

open class AndroidHeadless : Context() {
    override fun createPixmap(width: Int, height: Int): PixMap {
        return HeadlessPixMap(width, height)
    }

    override fun createViewBox(width: Int, height: Int): ViewBox {
        return HeadlessViewBox(width, height)
    }
    override var getCacheDirImpl: () -> String = {
        Client.context?.dataDir?.resolve("meteor-377/cache/")?.let {
            if (!it.exists())
                it.mkdirs()
            return@let it.absolutePath
        }
        throw RuntimeException("Could not locate suitable cache dir")
    }

    override fun createPix32(src: ByteArray): Pix32 {
        return Pix32()
    }
}