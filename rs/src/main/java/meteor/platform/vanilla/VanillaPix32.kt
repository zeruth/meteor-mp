package meteor.platform.vanilla

import jagex3.client.GameShell
import jagex3.graphics.Pix32
import java.awt.Image
import java.awt.MediaTracker
import java.awt.Toolkit
import java.awt.image.PixelGrabber

class VanillaPix32(data: ByteArray) : Pix32(data) {
    init {
        try {
            val c = GameShell.canvas
            val var3: Image = Toolkit.getDefaultToolkit().createImage(data)
            val var4 = MediaTracker(c)
            var4.addImage(var3, 0)
            var4.waitForAll()
            this.wi = var3.getWidth(c)
            this.hi = var3.getHeight(c)
            this.owi = this.wi
            this.ohi = this.hi
            this.xof = 0
            this.yof = 0
            this.data = IntArray(this.wi * this.hi)
            val var5 = PixelGrabber(var3, 0, 0, this.wi, this.hi, this.data, 0, this.wi)
            var5.grabPixels()
        } catch (ignore: InterruptedException) {
        }
    }
}