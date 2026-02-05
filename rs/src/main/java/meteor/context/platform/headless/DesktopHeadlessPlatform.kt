package meteor.context.platform.headless

import jagex2.client.ViewBox
import jagex2.graphics.Pix32
import jagex2.graphics.PixMap
import meteor.context.PlatformContext
import kotlin.reflect.KClass

open class DesktopHeadlessPlatform : PlatformContext() {
    override var viewBoxImplementation: KClass<out ViewBox> = HeadlessViewBox::class
    override var pixMapImplementation: KClass<out PixMap> = HeadlessPixMap::class
    override var getCacheDirImpl: () -> String = {
        System.getProperty("user.home") + "/meteor-377/cache/"
    }

    override fun createPix32(src: ByteArray): Pix32 {
        return Pix32()
    }
}

