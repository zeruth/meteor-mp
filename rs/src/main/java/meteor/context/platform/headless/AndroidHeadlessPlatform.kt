package meteor.context.platform.headless

import android.content.Context
import jagex2.client.Client
import jagex2.graphics.Pix32
import meteor.context.PlatformContext

open class AndroidHeadlessPlatform : PlatformContext() {
    init {
        viewBoxImplementation = HeadlessViewBox::class
        pixMapImplementation = HeadlessPixMap::class
    }

    companion object {
        init {
            getCacheDirImpl = ::getCacheDirImpl
        }

        fun getCacheDirImpl(): String {
            Client.context?.dataDir?.resolve("meteor-377/cache/")?.let {
                if (!it.exists())
                    it.mkdirs()
                return it.absolutePath
            }
            throw RuntimeException("Could not locate suitable cache dir")
        }
    }

    override fun createPix32(src: ByteArray): Pix32 {
        return Pix32()
    }
}