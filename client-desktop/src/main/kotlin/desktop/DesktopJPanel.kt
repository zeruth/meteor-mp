package desktop

import jagex3.client.input.keyboard.ClientKeyboardListener
import jagex3.jstring.Cp1252
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JPanel

class DesktopJPanel: JPanel(), KeyListener {
    override fun paintComponent(g: Graphics) {
        g.drawImage(Main.context().image, 0, 0, width, height, null)
    }

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
}