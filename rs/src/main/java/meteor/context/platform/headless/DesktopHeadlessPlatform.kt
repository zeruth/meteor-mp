package meteor.context.platform.headless

import jagex2.graphics.Pix32
import meteor.context.PlatformContext

open class DesktopHeadlessPlatform : PlatformContext() {
    init {
        viewBoxImplementation = HeadlessViewBox::class
        pixMapImplementation = HeadlessPixMap::class
    }

    companion object {
        init {
            getCacheDirImpl = ::getCacheDirImpl
        }

        fun getCacheDirImpl(): String {
            val s = System.getProperty("user.home") + "/meteor-377/cache/"
            return s
        }
    }

    override fun createPix32(src: ByteArray): Pix32 {
        return Pix32()
    }
}

