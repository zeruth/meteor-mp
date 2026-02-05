package meteor.context.platform.headless

import android.content.Context
import jagex2.client.Client
import jagex2.client.ViewBox
import jagex2.graphics.Pix32
import jagex2.graphics.PixMap
import meteor.context.PlatformContext
import kotlin.reflect.KClass

open class AndroidHeadlessPlatform : PlatformContext() {
    override var viewBoxImplementation: KClass<out ViewBox>  = HeadlessViewBox::class
    override var pixMapImplementation: KClass<out PixMap>  = HeadlessPixMap::class
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