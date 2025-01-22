package meteor.common.ui.components.sidebar

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import meteor.common.ui.components.panel.PanelComposables

open class SidebarButton(
    icon: ImageVector? = null,
    description: String? = null,
    tint: Color? = null,
    imageResource: String? = null,
    val actionButton: Boolean = false,
    val bottom: Boolean = false,
    val position: Int = 0
) {
    var icon = mutableStateOf(icon)
    var description = mutableStateOf(description)
    var tint = mutableStateOf(tint)
    var imageResource = mutableStateOf(imageResource)
    open fun onClick() {
        PanelComposables.content.value = null
        println("${javaClass.name} button click")
    }
}