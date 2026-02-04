package meteor

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import jagex2.client.Client
import meteor.context.events.AndroidPixMapDraw
import meteor.context.platform.android.AndroidViewBox
import util.GlobalEventBus

class MainActivity : ComponentActivity() {

    val state = mutableStateOf(false)

    init {
        GlobalEventBus.subscribe<AndroidPixMapDraw> {
            state.value = !state.value
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread {
            Client.context = applicationContext
            Client.main(emptyArray<String>())
        }.start()
        setContent {
            state.value
            GameSurface(Client.client.viewbox as? AndroidViewBox)
        }
    }

    override fun onResume() {
        super.onResume()
        Client.client?.let {
            Client.client.redrawFrame = true
            Client.client.redrawSidebar = true
            Client.client.redrawSideicons = true
            Client.client.redrawChatback = true
        }
    }


    val paint = android.graphics.Paint()

    @Composable
    fun GameSurface(viewBox: AndroidViewBox?) {
        viewBox ?: return

        Canvas(modifier = Modifier.fillMaxSize().background(Color.Cyan)) {
            drawIntoCanvas { canvas ->
                state.value

                val destRect = android.graphics.Rect(
                    0, 0,
                    size.width.toInt(), size.height.toInt()
                )

                canvas.nativeCanvas.drawBitmap(
                    viewBox.bitmap,
                    null,
                    destRect,
                    paint
                )
            }
        }
    }

}

