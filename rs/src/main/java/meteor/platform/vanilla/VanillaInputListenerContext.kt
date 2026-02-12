package meteor.platform.vanilla

import jagex2.client.GameShell
import java.awt.event.*

class VanillaInputListenerContext(val ctx: GameShell) : MouseListener, MouseMotionListener, KeyListener, FocusListener {
    override fun mousePressed(e: MouseEvent) {
        val x = e.getX()
        val y = e.getY()

        ctx.idleCycles = 0
        ctx.nextMouseClickX = x
        ctx.nextMouseClickY = y
        ctx.nextMouseClickTime = System.currentTimeMillis()

        try {
            // Java >8 no longer uses "isMetaDown" for right clicks
            if (e.getButton() == MouseEvent.BUTTON3) {
                ctx.nextMouseClickButton = 2
                ctx.mouseButton = 2
            } else {
                ctx.nextMouseClickButton = 1
                ctx.mouseButton = 1
            }
        } catch (ex: NoSuchMethodError) {
            if (e.isMetaDown()) {
                ctx.nextMouseClickButton = 2
                ctx.mouseButton = 2
            } else {
                ctx.nextMouseClickButton = 1
                ctx.mouseButton = 1
            }
        }
    }

    override fun mouseReleased(e: MouseEvent) {
        ctx.idleCycles = 0
        ctx.mouseButton = 0
    }

    override fun mouseExited(e: MouseEvent) {
        ctx.idleCycles = 0
        ctx.mouseX = -1
        ctx.mouseY = -1
    }

    override fun mouseDragged(e: MouseEvent) {
        val x = e.getX()
        val y = e.getY()

        ctx.idleCycles = 0
        ctx.mouseX = x
        ctx.mouseY = y
    }

    override fun mouseMoved(e: MouseEvent) {
        val x = e.getX()
        val y = e.getY()

        ctx.idleCycles = 0
        ctx.mouseX = x
        ctx.mouseY = y
    }

    override fun keyPressed(e: KeyEvent) {
        ctx.idleCycles = 0

        val code = e.getKeyCode()
        var ch = e.getKeyChar().code

        if (ch < 30) {
            ch = 0
        }

        when (code) {
            37 -> {
                ch = 1
            }
            39 -> {
                ch = 2
            }
            38 -> {
                ch = 3
            }
            40 -> {
                ch = 4
            }
            17 -> {
                ch = 5
            }
            8 -> {
                ch = 8
            }
            127 -> {
                ch = 8
            }
            9 -> {
                ch = 9
            }
            10 -> {
                ch = 10
            }
            in 112..123 -> {
                ch = code + 1008 - 112
            }
            36 -> {
                ch = 1000
            }
            35 -> {
                ch = 1001
            }
            33 -> {
                ch = 1002
            }
            34 -> {
                ch = 1003
            }
        }

        if (ch in 1..<128) {
            ctx.actionKey[ch] = 1
        }

        if (ch > 4) {
            ctx.keyQueue[ctx.keyQueueWritePos] = ch
            ctx.keyQueueWritePos = ctx.keyQueueWritePos + 1 and 0x7F
        }
    }

    override fun keyReleased(e: KeyEvent) {
        ctx.idleCycles = 0

        val code = e.getKeyCode()
        var ch = e.getKeyChar()

        if (ch.code < 30) {
            ch = 0.toChar()
        }

        when (code) {
            37 -> {
                ch = 1.toChar()
            }
            39 -> {
                ch = 2.toChar()
            }
            38 -> {
                ch = 3.toChar()
            }
            40 -> {
                ch = 4.toChar()
            }
            17 -> {
                ch = 5.toChar()
            }
            8 -> {
                ch = '\b'
            }
            127 -> {
                ch = '\b'
            }
            9 -> {
                ch = '\t'
            }
            10 -> {
                ch = '\n'
            }
        }

        if (ch.code in 1..<128) {
            ctx.actionKey[ch.code] = 0
        }
    }

    override fun focusGained(e: FocusEvent) {
        ctx.hasFocus = true
        ctx.redrawScreen = true
        ctx.refresh()
    }

    override fun focusLost(e: FocusEvent) {
        ctx.hasFocus = false
        for (i in 0..127) {
            ctx.actionKey[i] = 0
        }
    }

    override fun mouseClicked(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun keyTyped(e: KeyEvent) {}
}