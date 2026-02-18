package common.ui.toolbar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import common.ext.ModifierExt.onPress
import common.ui.ToolbarImpl

open class ToolbarButton {
    open var icon = mutableStateOf<ImageVector?>(null)
    open var drawerSize = mutableStateOf(200)
    var background = mutableStateOf(Color.Cyan)
    var expanded = mutableStateOf(false)
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun button() {
        Box(Modifier.clip(RoundedCornerShape(4.dp)).size(34.dp).background(background.value)
            .onPress { action() }) {
            content()
        }
    }

    @Composable
    open fun BoxScope.content() {
        Box(Modifier.size(30.dp).background(Color.Transparent).align(Alignment.Center)) {
            icon()
        }
    }

    @Composable
    fun BoxScope.icon() {
        Box(Modifier.background(Color.Transparent).align(Alignment.Center)) {
            icon.value?.let {
                Image(it, "")
            }
        }
    }

    open fun action() {
        if (!expanded.value) {
            if (ToolbarImpl.expanded.value != drawerSize.value) {
                ToolbarImpl.expanded.value = drawerSize.value
            }
            ToolbarImpl.drawerContext.value = this
            expanded.value = true
        } else {
            if (ToolbarImpl.drawerContext.value == this) {
                ToolbarImpl.expanded.value = 0
                expanded.value = false
            } else {
                if (ToolbarImpl.expanded.value != drawerSize.value) {
                    ToolbarImpl.expanded.value = drawerSize.value
                }
                ToolbarImpl.drawerContext.value = this
                expanded.value = true
            }
        }
    }

    @Composable
    open fun drawerContent() {}
}