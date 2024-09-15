package meteor.ui.compose.overlay

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import meteor.Constants
import meteor.Main
import meteor.Main.forceRecomposition
import meteor.ui.compose.components.GamePanel
import meteor.ui.compose.components.GamePanel.aspectMode
import meteor.ui.compose.components.GamePanel.containerSize
import meteor.ui.compose.components.GamePanel.scaleX
import meteor.ui.compose.components.GamePanel.scaleY
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



        val imageSize =
            IntSize(Constants.RS_DIMENSIONS.width, Constants.RS_DIMENSIONS.height)
        val containerSize = GamePanel.containerSize

        scaleX = containerSize.value.width.toFloat() / imageSize.width
        scaleY = containerSize.value.height.toFloat() / imageSize.height

        val fitScaleFactor = when (aspectMode.value) {
            AspectMode.FIT -> minOf(scaleX, scaleY)
            AspectMode.FILL -> maxOf(scaleX, scaleY)
        }
        var offsetX = 0
        var width = GamePanel.stretchedWidth.value
        var height = GamePanel.stretchedHeight.value
        offsetX = (width - ((imageSize.width.toFloat() / fitScaleFactor) * 2)).toInt()
        if (aspectMode.value == AspectMode.FIT) {
            width = imageSize.width.toFloat() / fitScaleFactor
            height = imageSize.height.toFloat() / fitScaleFactor
        }
        offsetX = ((containerSize.value.width - (width * 2)) / 2).toInt()
        var mod = Modifier.fillMaxSize()
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