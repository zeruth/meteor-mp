package meteor.input

import android.view.KeyEvent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.SoftwareKeyboardController
import meteor.MainActivity.Companion.clientInstance

object KeyboardController {
    lateinit var keyboardController: SoftwareKeyboardController
    var currentText = mutableStateOf("")
    var showTextInput = mutableStateOf(false)

    fun handleKeyEvent(event: KeyEvent): Boolean {
        println("received key event")
        var asciiKey = event.unicodeChar
        if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
            showTextInput.value = false
            keyboardController.hide()
            currentText.value = ""
        }

        if (event.keyCode == KeyEvent.KEYCODE_DEL) {
            asciiKey = 8
        }

        if (event.action == KeyEvent.ACTION_DOWN) {
            clientInstance.keyPressed(asciiKey, -1)
        } else if (event.action == KeyEvent.ACTION_UP) {
            clientInstance.keyReleased(asciiKey)
            clientInstance.keyTyped(asciiKey)
        }

        return false
    }
}