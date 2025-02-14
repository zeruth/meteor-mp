package meteor.platform.common

import android.content.Context
import meteor.platform.common.Common.isAndroid
import java.io.File

object Configuration {
    lateinit var dataDir: File

    init {
        if (!isAndroid) {
            dataDir = File(System.getProperty("user.home"), ".meteor-225/")
            if (!dataDir.exists()) {
                dataDir.mkdirs()
            }
        }
    }

    fun init(context: Context) {
        dataDir = File(context.dataDir, ".meteor-225/")

        if (!dataDir.exists()) {
            dataDir.mkdirs()
        }
    }
}