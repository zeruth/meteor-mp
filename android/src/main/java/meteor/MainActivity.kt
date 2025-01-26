package meteor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import client.client
import ext.android.ComponentActivityExt.setupActivity
import meteor.common.Common
import meteor.common.plugin.PluginManager
import meteor.input.KeyboardController.keyboardController
import meteor.ui.Window.MeteorViewBox
import java.io.File


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadMeteor()
        setContent {
            keyboardController = LocalSoftwareKeyboardController.current!!
            val focusRequester = remember { FocusRequester() }
            Box(modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)) {
                MeteorViewBox()
            }
        }
    }

    private fun loadMeteor() {
        setupActivity()
        setupMeteor()
        Game.start()
        PluginManager.start()
    }

    private fun setupMeteor() {
        Common.isAndroid = true
        Logger.logFile = File(dataDir, "log.txt")
        meteor.common.Configuration.init(applicationContext)
    }
}

