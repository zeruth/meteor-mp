package meteor.common.ui.components.sidebar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import meteor.common.ext.kotlin.MutableStateExt.toggle
import meteor.common.Common.panelOpen
import meteor.common.Common.sidebarWidth
import meteor.common.panel.PanelComposables.secondaryContent
import meteor.common.ui.Colors.secondary
import meteor.common.ui.Colors.surfaceDark
import meteor.common.ui.components.sidebar.buttons.PluginsButton

object SidebarComposables {
    val sidebarButtons = arrayListOf<SidebarButton>(PluginsButton())
    val padding = mutableStateOf(5.dp)
    val buttonSize = mutableStateOf(sidebarWidth.value - padding.value)
    var lastButtonClicked = mutableStateOf<SidebarButton?>(null)

    inline fun<reified T> getButton(): T {
        return sidebarButtons.filterIsInstance<T>().first()
    }

    fun add(sidebarButton: SidebarButton) {
        if (!sidebarButtons.contains(sidebarButton)) {
            sidebarButtons.add(sidebarButton)
        }
    }

    fun remove(sidebarButton: SidebarButton) {
        if (sidebarButtons.contains(sidebarButton)) {
            sidebarButtons.remove(sidebarButton)
        }
    }

    @Composable
    fun Sidebar(vararg platformButtons: SidebarButton) {
        val allButtons = platformButtons.toMutableSet()
        platformButtons.forEach { add(it) }
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
        Row(Modifier.fillMaxWidth().height(buttonSize.value - padding.value)) {
            Box(
                Modifier.clip(RoundedCornerShape(5.dp)).fillMaxSize().background(sidebarButton.tint.value?: secondary.value )
                    .clickable {
                        buttonClick(
                            sidebarButton
                        )
                    }) {
                sidebarButton.icon.value?.let { Image(it, contentDescription = null) }
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

    @Composable
    fun SidebarButton() {
        Box(Modifier.fillMaxWidth().height(buttonSize.value).background(secondary.value)) {

        }
    }
}