package meteor.platform.vanilla

import jagex2.client.Client
import jagex2.client.ViewBox
import meteor.events.VanillaPixMapDraw
import sign.signlink
import util.GlobalEventBus
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

class VanillaViewBox(width: Int, height: Int) : ViewBox(Client.client, width, height) {
    private val frame = JFrame()
    private val panel: JPanel

    companion object {
        private var buffer: BufferedImage? = null
    }
    init {
        if (buffer == null)
            buffer = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        panel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                g.drawImage(buffer, 0, 0, buffer!!.width, buffer!!.height, null)
            }
        }
        panel.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                if (panel.width > 0 && panel.height > 0) {
                    with(shell as Client) {
                        redrawScreen = true
                        redrawSideicons = true
                        redrawFrame = true
                        redrawSidebar = true
                        redrawChatback = true
                        redrawPrivacySettings = true
                    }
                }
            }
        })


        SwingUtilities.invokeLater {
            frame.title = "RS2 user client - release #${signlink.clientversion}"
            frame.isResizable = false
            frame.background = Color.black
            frame.size = Dimension(buffer!!.width, buffer!!.height)
            frame.layout = BorderLayout()
            frame.add(panel, null)
            frame.isUndecorated = true
            frame.isVisible = true
            frame.toFront()

            val inputListener = VanillaInputListenerContext(shell)
            frame.addKeyListener(inputListener)
            panel.addMouseListener(inputListener)
            panel.addMouseMotionListener(inputListener)
        }

        GlobalEventBus.subscribe<VanillaPixMapDraw> {
            getGraphics()?.drawImage(it.payload.image, it.payload.x, it.payload.y, null)
        }
    }

    fun getGraphics(): Graphics? = panel.graphics
}