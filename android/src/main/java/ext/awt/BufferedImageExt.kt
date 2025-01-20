package ext.awt

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.awt.image.BufferedImage

object BufferedImageExt {
    fun BufferedImage.toComposeImageBitmap(): ImageBitmap {
        // TODO(demin): use toBitmap().asImageBitmap() from skiko, when we fix its performance
        //  (it is 40x slower)

        val bytesPerPixel = 4
        val pixels = IntArray(width * height * bytesPerPixel)

        var k = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                val argb = getRGB(x, y)
                // Directly write BGRA to the pixel array
                pixels[k++] = (argb and 0xff)           // Blue
                pixels[k++] = (argb shr 8 and 0xff)     // Green
                pixels[k++] = (argb shr 16 and 0xff)    // Red
                pixels[k++] = (argb shr 24 and 0xff)    // Alpha
            }
        }

        val bitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.RGB_565).asImageBitmap()
        return bitmap
    }
}