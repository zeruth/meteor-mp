package meteor.platform.common.ui.components.sidebar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import client.Client.isAndroid
import meteor.platform.common.ext.kotlin.MutableStateExt.toggle
import meteor.platform.common.ui.components.panel.PanelComposables.secondaryContent
import meteor.platform.common.ui.Colors.secondary
import meteor.platform.common.ui.Colors.surfaceDark
import meteor.platform.common.ui.UI.panelOpen
import meteor.platform.common.ui.UI.sidebarWidth
import meteor.platform.common.ui.components.sidebar.buttons.PluginsButton

object SidebarComposables {
    val sidebarButtons = mutableSetOf<SidebarButton>(PluginsButton())
    val padding = mutableStateOf(5.dp)
    val buttonSize = mutableStateOf(sidebarWidth.value - padding.value)
    var lastButtonClicked = mutableStateOf<SidebarButton?>(null)

    inline fun<reified T> getButton(): T? {
        return sidebarButtons.filterIsInstance<T>().firstOrNull()
    }

    @Composable
    fun Sidebar(vararg platformButtons: SidebarButton) {
        val allButtons = platformButtons.toMutableSet()
        allButtons.addAll(sidebarButtons)
        //Sidebar
        Box(Modifier.width(sidebarWidth.value).fillMaxHeight().background(surfaceDark.value)) {
            //Buttons
            Column(Modifier.fillMaxSize().padding(all = padding.value)) {
                for (sidebarButton in allButtons.filter { !it.bottom }.sortedBy { it.position }) {
                    SidebarButtonNode(
                        sidebarButton
                    )
                }
                Spacer(Modifier.weight(1f))
                for (sidebarButton in allButtons.filter { it.bottom }.sortedBy { it.position }) {
                    SidebarButtonNode(
                        sidebarButton
                    )
                }
            }
        }
    }

    @Composable
    fun SidebarButtonNode(sidebarButton: SidebarButton) {
        var mod = Modifier.clip(RoundedCornerShape(5.dp)).fillMaxSize().background(sidebarButton.tint.value?: secondary.value )
        if (isAndroid) {
            mod = mod.clickable {
                buttonClick(
                    sidebarButton
                )
            }
        }
        else {
            mod = mod.pointerInput(Unit) {
                detectTapGestures {
                    buttonClick(
                        sidebarButton
                    )
                }
            }
        }
        Row(Modifier.fillMaxWidth().height(buttonSize.value - padding.value)) {
            Box(mod) {
                if (sidebarButton.content() != null) {
                    sidebarButton.content()?.invoke(this)
                } else
                    sidebarButton.icon?.let { Image(it.value, contentDescription = null) }
            }
        }
        Spacer(Modifier.height(padding.value))
    }

    fun buttonClick(button: SidebarButton) {
        if (button.actionButton) {
            button.onClick()
            return
        }
        if (lastButtonClicked.value == button) {
            if (secondaryContent.value != null) {
                secondaryContent.value = null
            } else {
                panelOpen.toggle()
            }
        }
        if (lastButtonClicked.value == null) {
            panelOpen.value = true
        } else {
            if (lastButtonClicked.value != button) {
                secondaryContent.value = null
                panelOpen.value = true
            }
        }
        if (panelOpen.value)
            button.onClick()
        lastButtonClicked.value = button
    }
}