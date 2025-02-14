package meteor.platform.android.ui.batteryfps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.BatteryEmptySolid
import compose.icons.lineawesomeicons.BatteryFullSolid
import compose.icons.lineawesomeicons.BatteryHalfSolid
import compose.icons.lineawesomeicons.BatteryQuarterSolid
import compose.icons.lineawesomeicons.BatteryThreeQuartersSolid
import meteor.platform.android.Game.fps
import meteor.platform.common.Common.eventbus
import meteor.platform.common.ui.components.sidebar.SidebarButton
import meteor.platform.android.events.BatteryLevelChanged

val full = LineAwesomeIcons.BatteryFullSolid
val threeQuarters = LineAwesomeIcons.BatteryThreeQuartersSolid
val half = LineAwesomeIcons.BatteryHalfSolid
val quarter = LineAwesomeIcons.BatteryQuarterSolid
val empty = LineAwesomeIcons.BatteryEmptySolid

val icon = mutableStateOf(full)
val batteryLevel = mutableIntStateOf(100)

class BatteryFpsDisplayButton: SidebarButton(icon = icon, bottom = true, actionButton = true) {
    override fun content(): @Composable (BoxScope.() -> Unit) = @Composable {
        Column(Modifier.align(Alignment.Center)) {
            Row(modifier = Modifier.fillMaxHeight(.5f).align(Alignment.CenterHorizontally)) {
                Image(icon!!.value, contentDescription = null, modifier = Modifier.align(Alignment.CenterVertically))
                Text("${batteryLevel.intValue}", fontSize = 8.sp, modifier = Modifier.align(Alignment.CenterVertically))
            }
            Text("FPS:${fps.intValue}", fontSize = 8.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
    companion object {
        init {
            eventbus.subscribe<BatteryLevelChanged> {
                updateBatteryIcon(it.data.level)
            }
        }

        fun updateBatteryIcon(percent: Int) {
            when (percent) {
                in 80..100 -> icon.value = full
                in 60..79 -> icon.value = threeQuarters
                in 40..59 -> icon.value = half
                in 20..39 -> icon.value = quarter
                in 0..19 -> icon.value = empty
            }
            batteryLevel.intValue = percent
        }
    }
}