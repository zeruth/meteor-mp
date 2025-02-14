package ext.android

import android.content.pm.ActivityInfo
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import meteor.platform.android.input.KeyboardController.showTextInput

object ComponentActivityExt {
    fun ComponentActivity.hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    fun ComponentActivity.setupActivity() {
        onBackPressedDispatcher.addCallback {
            showTextInput.value = false
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        hideSystemUI()
        enableEdgeToEdge()
    }
}