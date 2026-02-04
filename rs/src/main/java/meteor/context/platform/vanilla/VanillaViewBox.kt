package meteor.context.platform.vanilla

import jagex2.client.Client
import jagex2.client.GameShell
import jagex2.client.ViewBox
import meteor.context.events.VanillaPixMapDraw
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

class VanillaViewBox(width: Int, height: Int, shell: GameShell) : ViewBox(shell, width, height) {
    private val frame = JFrame()
    private val panel: JPanel

    companion object {
        private var buffer: BufferedImage? = null

    }
    init {
        this.shell = shell
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
                    val client = shell as Client
                    shell.redrawScreen = true
                    client.redrawSideicons = true
                    client.redrawFrame = true
                    client.redrawSidebar = true
                    client.redrawChatback = true
                    client.redrawPrivacySettings = true
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