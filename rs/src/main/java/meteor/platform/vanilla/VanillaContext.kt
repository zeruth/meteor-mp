package meteor.platform.vanilla

import jagex3.client.GameCanvas
import jagex3.client.GameShell
import meteor.platform.Context
import java.awt.Color
import java.awt.Container
import java.awt.Frame
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import kotlin.system.exitProcess

class VanillaContext : Context, FocusListener, WindowListener {
    var frame = Frame()

    override fun startApplication(width: Int, height: Int) {
        frame.setTitle("Jagex")
        frame.setResizable(false)
        frame.setBackground(Color.BLACK)
        frame.addWindowListener(this)
        frame.isVisible = true
        frame.toFront()
        val insets = frame.insets
        frame.setSize(width + insets.left + insets.right, height + insets.top + insets.bottom)
    }

    override fun addcanvas() {
        val var1: Container = frame
        if (GameShell.canvas != null) {
            GameShell.canvas.removeFocusListener(this)
            var1.remove(GameShell.canvas)
        }
        GameShell.canvas = GameCanvas(GameShell.shell) //TODO: this
        var1.add(GameShell.canvas)
        GameShell.canvas.setSize(GameShell.sWid, GameShell.sHei)
        GameShell.canvas.isVisible = true
        val var2 = frame.insets
        GameShell.canvas.setLocation(var2.left, var2.top)
        GameShell.canvas.addFocusListener(this)
        GameShell.canvas.requestFocus()
    }

    override fun mainredrawwrapper() {
        GameShell.canvas.setSize(GameShell.sWid, GameShell.sHei)
        GameShell.canvas.isVisible = true
        val var6 = frame.insets
        GameShell.canvas.setLocation(var6.left, var6.top)
    }

    override fun shutdown() {
        try {
            GameShell.canvas.removeFocusListener(this)
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

    //FocusListener

    override fun focusGained(e: FocusEvent?) {
        GameShell.focus_in = true
        GameShell.fullredraw = true
    }

    override fun focusLost(e: FocusEvent?) {
        GameShell.focus_in = false
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
}