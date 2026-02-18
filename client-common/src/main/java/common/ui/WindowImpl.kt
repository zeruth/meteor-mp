package common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

object WindowImpl {
    @Composable
    fun Window(content: @Composable () -> Unit) {
        //Container
        Box(Modifier.fillMaxSize().background(Color.DarkGray)) {
            //Container Layout
            Row(Modifier.fillMaxSize()) {
                //Game View
                Box(Modifier.weight(1f).background(Color.Transparent)) {
                    content.invoke()
                }
                ToolbarImpl.Toolbar()
            }
        }
    }
}