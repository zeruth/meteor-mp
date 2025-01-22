package meteor.common.ui.components.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import meteor.common.ui.Colors.secondarySurface

object PanelComposables {
    /**
     * The main composable panel that is shown when opened
     */
    val content: MutableState<(@Composable () -> Unit)?> = mutableStateOf(null)

    /**
     * A secondary composable panel that takes priority over the first if it isn't null
     */
    val secondaryContent: MutableState<(@Composable () -> Unit)?> = mutableStateOf(null)

    @Composable
    fun Panel() {
        Box(modifier = Modifier.fillMaxSize().background(secondarySurface.value)) {
            if (secondaryContent.value != null) {
                secondaryContent.value!!.invoke()
            } else {
                content.value?.invoke()
            }
        }
    }
}