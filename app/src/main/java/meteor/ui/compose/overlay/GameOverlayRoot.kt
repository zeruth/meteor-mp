package meteor.ui.compose.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import meteor.Constants
import meteor.Main
import meteor.Main.forceRecomposition
import meteor.ui.compose.components.GamePanel
import meteor.ui.compose.components.GamePanel.aspectMode
import meteor.ui.compose.components.GamePanel.sX
import meteor.ui.compose.components.GamePanel.sY
import meteor.ui.compose.components.GamePanel.stretchedHeight
import meteor.ui.compose.components.GamePanel.stretchedWidth
import meteor.ui.compose.events.PreRender
import meteor.ui.compose.overlay.Overlay.Companion.debugOverlays
import meteor.ui.config.AspectMode
import org.rationalityfrontline.kevent.KEVENT

object GameOverlayRoot {
    val gameOverlays = ArrayList<GameOverlay>()
    /**
     * This overlay layer covers the entire game area
     */
    @Composable
    fun render() {
        forceRecomposition.value
        KEVENT.post(PreRender)
        val compositionStart = System.currentTimeMillis()

        val w = (Constants.RS_DIMENSIONS.width * sX)
        val h = (Constants.RS_DIMENSIONS.height * sY)
        val fitWidth = w - stretchedWidth.floatValue
        val fitHeight = h - stretchedHeight.floatValue
        val padX = fitWidth / 2
        val padY = fitHeight / 2

        var mod = Modifier.absoluteOffset(x = 0.dp, y = 0.dp)
        if (Main.client.aspectMode == AspectMode.FILL) {
            mod = mod.size(w.dp, h.dp)
        } else {

            mod = mod.size(stretchedWidth.floatValue.dp, stretchedHeight.floatValue.dp)

            if (sX > sY) {
                mod = mod.offset(x = padX.dp)
            } else {
                mod = mod.offset(y = padY.dp)
            }
        }

        if (/*Main.client.isLoggedIn() && */debugOverlays.value)
            mod = mod.background(Color.Cyan.copy(alpha = .2f))
        Box(mod) {
            for (gameOverlay in gameOverlays) {
                gameOverlay.render().invoke(this)
            }
            ViewportOverlayRoot.render()
        }
        Main.composeTime.longValue = System.currentTimeMillis() - compositionStart
    }
}