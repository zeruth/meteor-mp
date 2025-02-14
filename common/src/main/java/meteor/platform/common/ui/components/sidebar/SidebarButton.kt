package meteor.platform.common.ui.components.sidebar

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import meteor.platform.common.Common.logger
import meteor.platform.common.ui.components.panel.PanelComposables

open class SidebarButton(
    val icon: MutableState<ImageVector>? = null,
    description: String? = null,
    tint: Color? = null,
    imageResource: String? = null,
    val actionButton: Boolean = false,
    val bottom: Boolean = false,
    val position: Int = 0
) {
    var description = mutableStateOf(description)
    var tint = mutableStateOf(tint)
    var imageResource = mutableStateOf(imageResource)

    open fun content(): (@Composable BoxScope.() -> Unit)? = null

    open fun onClick() {
        PanelComposables.content.value = null
    }
}