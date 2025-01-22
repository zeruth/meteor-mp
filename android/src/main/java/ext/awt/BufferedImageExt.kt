package ext.awt

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import meteor.MainActivity.Companion.clientInstance
import java.awt.image.BufferedImage

object BufferedImageExt {
    fun BufferedImage.toComposeImageBitmap(): ImageBitmap? {
        clientInstance.drawArea?.pixels?.let {
            val bitmap = Bitmap.createBitmap(it, width, height, Bitmap.Config.RGB_565).asImageBitmap()
            return bitmap
        }
        return null
    }
}