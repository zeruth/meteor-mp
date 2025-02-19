package meteor.platform.desktop.ui.buttons

import androidx.compose.runtime.mutableStateOf
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.MapSolid
import meteor.platform.common.ext.kotlin.MutableStateExt.toggle
import meteor.platform.common.ui.components.sidebar.SidebarButton
import meteor.platform.desktop.ui.MapView

class MapViewButton : SidebarButton(icon = mutableStateOf(LineAwesomeIcons.MapSolid), actionButton = true) {
    override fun onClick() {
        MapView.mapVisible.toggle()
    }
}