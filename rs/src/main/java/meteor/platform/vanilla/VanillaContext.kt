package meteor.platform.vanilla

import jagex3.client.GameShell
import jagex3.client.applet.GameShellCache
import jagex3.client.input.keyboard.ClientKeyboardListener
import jagex3.client.input.mouse.ClientMouseListener
import jagex3.client.input.mouse.ClientMouseWheelListener
import jagex3.graphics.Pix32
import jagex3.graphics.PixMap
import jagex3.io.FileOnDisk
import jagex3.io.Packet
import jagex3.jstring.Cp1252
import jagex3.sound.JavaPcmPlayer
import jagex3.util.MonotonicTime
import meteor.platform.Context
import meteor.platform.ContextCommon
import java.awt.*
import java.awt.event.*
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.net.URL
import kotlin.system.exitProcess

class VanillaContext :
    Context, FocusListener, WindowListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    var frame = Frame()
    var canvas: Canvas? = null
    private var progressFont: Font? = null
    private var progressBar: Image? = null
    private var progressFontMetrics: FontMetrics? = null

    fun graphics(): Graphics = canvas!!.graphics

    override fun startApplication(width: Int, height: Int) {
        val context = this
        with (frame) {
            setTitle("Jagex")
            setResizable(false)
            setBackground(Color.BLACK)
            addWindowListener(context)
            isVisible = true
            toFront()
            val insets = frame.insets
            setSize(width + insets.left + insets.right, height + insets.top + insets.bottom)
        }
    }

    override fun addcanvas() {
        val context = this
        val var1: Container = frame
        if (canvas != null) {
            canvas?.removeFocusListener(context)
            var1.remove(canvas)
        }
        canvas = Canvas() //TODO: this
        canvas?.let {
            with (it) {
                var1.add(this)
                setSize(GameShell.sWid, GameShell.sHei)
                isVisible = true
                val var2 = frame.insets
                setLocation(var2.left, var2.top)
                addFocusListener(context)
                requestFocus()
            }
        }
    }

    override fun mainredrawwrapper() {
        canvas?.let {
            with (it) {
                setSize(GameShell.sWid, GameShell.sHei)
                isVisible = true
                val var6 = frame.insets
                setLocation(var6.left, var6.top)
            }
        }
    }

    override fun shutdown() {
        try {
            canvas?.removeFocusListener(this)
        } catch (ignore: Exception) {
        }

        try {
            GameShell.shell.mainquit()
        } catch (ignore: Exception) {
        }

        try {
            exitProcess(0)
        } catch (ignore: Throwable) {
        }
    }

    override fun addKeyListeners() {
        val context = this
        canvas?.let {
            with (it) {
                setFocusTraversalKeysEnabled(false)
                addKeyListener(context)
                addFocusListener(context)
            }
        }
    }

    override fun removeKeyListeners() {
        val context = this
        canvas?.let {
            with (it) {
                removeKeyListener(context)
                removeFocusListener(context)
            }
        }
    }

    override fun addMouseListeners() {
        val context = this
        canvas?.let {
            with (it) {
                addMouseListener(context)
                addMouseMotionListener(context)
                addFocusListener(context)
            }
        }
    }

    override fun removeMouseListeners() {
        val context = this
        canvas?.let {
            with (it) {
                removeMouseListener(context)
                removeMouseMotionListener(context)
                removeFocusListener(context)
            }
        }
    }

    val mouseWheelListeners = ArrayList<ClientMouseWheelListener>()

    override fun addMouseWheelListener(iface: ClientMouseWheelListener) {
        mouseWheelListeners.add(iface)
        canvas?.addMouseWheelListener(this)
    }

    override fun removeMouseWheelListener(iface: ClientMouseWheelListener) {
        mouseWheelListeners.remove(iface)
        canvas?.removeMouseWheelListener(this)
    }

    override fun createPixMap(width: Int, height: Int): PixMap {
        return VanillaPixMap(this, width, height).apply {
            create(width, height)
        }
    }

    override fun createPix32(data: ByteArray): Pix32 {
        return VanillaPix32(data, this)
    }

    override fun repaintCanvas() {
/*        try {
            GameShell.drawArea.draw(0, 0)
        } catch (var25: java.lang.Exception) {
            canvas?.repaint()
        }*/
    }

    override fun repaint() {
        //canvas?.repaint()
    }

    override fun drawProgress(progress: Int, message: String) {
        try {
            val color = Color(140, 17, 17)
            val g = canvas?.graphics!!

            if (progressFont == null) {
                progressFont = Font("Helvetica", Font.BOLD, 13)
                progressFontMetrics = canvas?.getFontMetrics(progressFont)
            }

            if (GameShell.fullredraw) {
                GameShell.fullredraw = false
                g.color = Color.black
                g.fillRect(0, 0, GameShell.sWid, GameShell.sHei)
            }

            try {
                if (progressBar == null) {
                    progressBar = canvas?.createImage(304, 34)
                }

                val bar = progressBar!!.graphics

                bar.color = color
                bar.drawRect(0, 0, 303, 33)
                bar.fillRect(2, 2, progress * 3, 30)

                bar.color = Color.black
                bar.drawRect(1, 1, 301, 31)
                bar.fillRect(progress * 3 + 2, 2, 300 - progress * 3, 30)

                bar.font = progressFont
                bar.color = Color.white
                bar.drawString(message, (304 - progressFontMetrics!!.stringWidth(message)) / 2, 22)

                g.drawImage(progressBar, GameShell.sWid / 2 - 152, GameShell.sHei / 2 - 18, null)
            } catch (ex: java.lang.Exception) {
                val x = GameShell.sWid / 2 - 152
                val y = GameShell.sHei / 2 - 18

                g.color = color
                g.drawRect(x, y, 303, 33)
                g.fillRect(x + 2, y + 2, progress * 3, 30)

                g.color = Color.black
                g.drawRect(x + 1, y + 1, 301, 31)
                g.fillRect(progress * 3 + x + 2, y + 2, 300 - progress * 3, 30)

                g.font = progressFont
                g.color = Color.white
                g.drawString(message, x + (304 - progressFontMetrics!!.stringWidth(message)) / 2, y + 22)
            }
        } catch (ex: java.lang.Exception) {
            canvas?.repaint()
        }
    }

    override fun resetProgress() {
        progressBar = null
        progressFont = null
        progressFontMetrics = null
    }

    override fun showDocument(url: URL, sub: String) {
        TODO("Not yet implemented")
    }

    override fun keepThreadAlive() = false

    override fun getCodeBase(): URL {
        return ContextCommon.getCodeBase()
    }

    override fun getParameter(name: String): String? {
       return ContextCommon.getParameter(name)
    }

    override fun getCacheDirectory(): String {
        return System.getProperty("user.home") + "/.meteor-os1"
    }

    override fun createPcmPlayer(): JavaPcmPlayer {
        return VanillaPcmPlayer()
    }

    override fun onDraw() {

    }

    //FocusListener

    override fun focusGained(e: FocusEvent?) {
        GameShell.focus_in = true
        GameShell.fullredraw = true
    }

    override fun focusLost(e: FocusEvent?) {
        GameShell.focus_in = false

        if (ClientKeyboardListener.instance != null) {
            ClientKeyboardListener.keyHeldReadPos = -1
        }

        if (ClientMouseListener.instance != null) {
            ClientMouseListener.nextMouseButton = 0
        }
    }

    //WindowListener

    override fun windowOpened(e: WindowEvent?) {}

    override fun windowClosing(e: WindowEvent?) {
        GameShell.shell.destroy()
    }

    override fun windowClosed(e: WindowEvent?) {}

    override fun windowIconified(e: WindowEvent?) {}

    override fun windowDeiconified(e: WindowEvent?) {}

    override fun windowActivated(e: WindowEvent?) {}

    override fun windowDeactivated(e: WindowEvent?) {}

    //KeyListener

    override fun keyTyped(e: KeyEvent?) {
        if (ClientKeyboardListener.instance != null) {
            val ch = e!!.getKeyChar()
            if (ch.code != 0 && ch.code != 65535 && Cp1252.canEncodeToCp1252(ch)) {
                val next = ClientKeyboardListener.keyWritePos + 1 and 0x7F
                if (ClientKeyboardListener.keyReadPos != next) {
                    ClientKeyboardListener.keyCodeBuffer[ClientKeyboardListener.keyWritePos] = -1
                    ClientKeyboardListener.keyChBuffer[ClientKeyboardListener.keyWritePos] = ch
                    ClientKeyboardListener.keyWritePos = next
                }
            }
        }

        e!!.consume()
    }

    override fun keyPressed(e: KeyEvent?) {
        if (ClientKeyboardListener.instance == null) {
            return
        }

        ClientKeyboardListener.idleTimer = 0

        val code = e!!.getKeyCode()
        var ch: Int
        if (code >= 0 && code < ClientKeyboardListener.KEY_CODE_MAP.size) {
            ch = ClientKeyboardListener.KEY_CODE_MAP[code]
            if ((ch and 0x80) != 0) {
                ch = -1
            }
        } else {
            ch = -1
        }

        if (ClientKeyboardListener.keyHeldReadPos >= 0 && ch >= 0) {
            ClientKeyboardListener.keyHeldBuffer[ClientKeyboardListener.keyHeldReadPos] = ch
            ClientKeyboardListener.keyHeldReadPos = ClientKeyboardListener.keyHeldReadPos + 1 and 0x7F

            if (ClientKeyboardListener.keyHeldWritePos == ClientKeyboardListener.keyHeldReadPos) {
                ClientKeyboardListener.keyHeldReadPos = -1
            }
        }

        if (ch >= 0) {
            val next = ClientKeyboardListener.keyWritePos + 1 and 0x7F
            if (ClientKeyboardListener.keyReadPos != next) {
                ClientKeyboardListener.keyCodeBuffer[ClientKeyboardListener.keyWritePos] = ch
                ClientKeyboardListener.keyChBuffer[ClientKeyboardListener.keyWritePos] = 0.toChar()
                ClientKeyboardListener.keyWritePos = next
            }
        }

        val mod = e.getModifiers()
        if ((mod and 0xA) != 0 || ch == 85 || ch == 10) {
            e.consume()
        }
    }

    override fun keyReleased(e: KeyEvent?) {
        if (ClientKeyboardListener.instance != null) {
            ClientKeyboardListener.idleTimer = 0

            val code = e!!.getKeyCode()
            val ch: Int
            if (code >= 0 && code < ClientKeyboardListener.KEY_CODE_MAP.size) {
                ch = ClientKeyboardListener.KEY_CODE_MAP[code] and -0x81
            } else {
                ch = -1
            }

            if (ClientKeyboardListener.keyHeldReadPos >= 0 && ch >= 0) {
                ClientKeyboardListener.keyHeldBuffer[ClientKeyboardListener.keyHeldReadPos] = ch.inv()
                ClientKeyboardListener.keyHeldReadPos = ClientKeyboardListener.keyHeldReadPos + 1 and 0x7F

                if (ClientKeyboardListener.keyHeldWritePos == ClientKeyboardListener.keyHeldReadPos) {
                    ClientKeyboardListener.keyHeldReadPos = -1
                }
            }
        }

        e!!.consume()
    }

    //MouseListener

    override fun mouseClicked(e: MouseEvent?) {
        if (e!!.isPopupTrigger()) {
            e.consume()
        }
    }

    override fun mousePressed(e: MouseEvent?) {
        if (ClientMouseListener.instance != null) {
            ClientMouseListener.idleTimer = 0

            ClientMouseListener.nextMouseClickX = e!!.getX()
            ClientMouseListener.nextMouseClickY = e.getY()
            ClientMouseListener.nextMouseClickTime = MonotonicTime.currentTime()

            if (e.getButton() == MouseEvent.BUTTON3) {
                ClientMouseListener.nextMouseClickButton = 2
                ClientMouseListener.nextMouseButton = 2
            } else {
                ClientMouseListener.nextMouseClickButton = 1
                ClientMouseListener.nextMouseButton = 1
            }
        }

        if (e!!.isPopupTrigger()) {
            e.consume()
        }
    }

    override fun mouseReleased(e: MouseEvent?) {

        if (ClientMouseListener.instance != null) {
            ClientMouseListener.idleTimer = 0

            ClientMouseListener.nextMouseButton = 0
        }

        if (e!!.isPopupTrigger()) {
            e.consume()
        }
    }

    override fun mouseEntered(e: MouseEvent?) {
        if (ClientMouseListener.instance != null) {
            ClientMouseListener.idleTimer = 0

            ClientMouseListener.nextMouseX = e!!.getX()
            ClientMouseListener.nextMouseY = e.getY()
        }
    }

    override fun mouseExited(e: MouseEvent?) {
        if (ClientMouseListener.instance != null) {
            ClientMouseListener.idleTimer = 0

            ClientMouseListener.nextMouseX = -1
            ClientMouseListener.nextMouseY = -1
        }
    }

    //MouseMotionListener

    override fun mouseDragged(e: MouseEvent?) {
        if (ClientMouseListener.instance != null) {
            ClientMouseListener.idleTimer = 0

            ClientMouseListener.nextMouseX = e!!.getX()
            ClientMouseListener.nextMouseY = e.getY()
        }
    }

    override fun mouseMoved(e: MouseEvent?) {
        if (ClientMouseListener.instance != null) {
            ClientMouseListener.idleTimer = 0

            ClientMouseListener.nextMouseX = e!!.getX()
            ClientMouseListener.nextMouseY = e.getY()
        }
    }

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        mouseWheelListeners.forEach {
            it.rotation += e.getWheelRotation()
        }
    }
}