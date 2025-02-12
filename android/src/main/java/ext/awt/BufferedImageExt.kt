package ext.awt

import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt

object BufferedImageExt {
    fun BufferedImage.getPixels(): IntArray {
        return (raster.dataBuffer as DataBufferInt).data
    }
}