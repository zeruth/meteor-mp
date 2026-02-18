package common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.ui.toolbar.ToolbarButton
import common.ui.toolbar.buttons.PluginsButton
import common.ui.toolbar.buttons.SettingsButton

object ToolbarImpl {

    val upperButtons = mutableListOf<ToolbarButton>()
    val lowerButtons = mutableListOf<ToolbarButton>()

    val expanded = mutableStateOf(0)
    val drawerContext = mutableStateOf<ToolbarButton?>(null)

    init {
        upperButtons.add(PluginsButton())
        lowerButtons.add(SettingsButton())
    }

    @Composable
    fun Toolbar() {
        if (expanded.value > 0 && drawerContext.value != null) {
            Box(Modifier.fillMaxHeight().width(expanded.value.dp).background(Colors.surface.value)) {
                drawerContext.value?.drawerContent()
            }
        }
        val mod = Modifier.fillMaxHeight().width(38.dp).background(Colors.surfaceDarker.value)
        Box(mod) {
            Column(Modifier.fillMaxSize().padding(2.dp)) {
                upperButtons.forEach {
                    it.button()
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.weight(1f))
                lowerButtons.forEach {
                    it.button()
                    Spacer(Modifier.height(2.dp))
                }
            }
        }
    }
}