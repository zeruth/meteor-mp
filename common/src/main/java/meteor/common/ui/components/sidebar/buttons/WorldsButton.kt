package meteor.common.ui.components.sidebar.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.GlobeSolid
import compose.icons.lineawesomeicons.StarSolid
import meteor.common.Common
import meteor.common.ui.Colors
import meteor.common.ui.components.panel.PanelComposables
import meteor.common.ui.components.sidebar.SidebarButton
import meteor.common.ui.components.sidebar.buttons.World.Companion.getFlag

class WorldsButton : SidebarButton(icon = LineAwesomeIcons.GlobeSolid) {
    override fun onClick() {
        PanelComposables.content.value = WorldsList()
    }

    companion object {
        var currentWorld = mutableIntStateOf(1);

        fun WorldsList(): @Composable (() -> Unit)  = @Composable {
            Box(modifier = Modifier.fillMaxSize()) {
                Row {
                    Box(
                        Modifier.clip(RoundedCornerShape(5.dp)).background(Colors.surfaceDark.value).fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                "2004Scape.org",
                                color = Color.White,
                                modifier = Modifier.align(Alignment.CenterHorizontally).height(20.dp)
                            )
                            for (world in World.worlds) {
                                Row(
                                    Modifier.height(20.dp)
                                        .background(if (currentWorld.value == world.id) Colors.surfaceDarker.value else Colors.surface.value)
                                        .clickable {
                                            client.client.members = world.members
                                            client.client.updateServerConnection(world.address, world.portOffset)
                                            currentWorld.value = world.id
                                        }) {
                                    Box(modifier = Modifier.fillMaxWidth(.15f)) {
                                        Text(
                                            "${world.id}",
                                            modifier = Modifier.align(Alignment.Center),
                                            color = Color.White
                                        )
                                    }
                                    Spacer(Modifier.width(5.dp).fillMaxHeight().background(Colors.surfaceDark.value))
                                    Box(modifier = Modifier.background(Colors.surfaceDark.value).fillMaxWidth(.15f)) {
                                        Image(
                                            LineAwesomeIcons.StarSolid,
                                            "members",
                                            modifier = Modifier.background(Colors.surfaceDark.value).fillMaxSize(),
                                            colorFilter = ColorFilter.tint(if (world.members) Color.Yellow else Color.White)
                                        )
                                    }
                                    if (!Common.isAndroid) {
                                        Spacer(Modifier.width(5.dp).fillMaxHeight().background(Colors.surfaceDark.value))
                                        Box(modifier = Modifier.background(Colors.surfaceDark.value).fillMaxWidth(.15f)) {
                                            Image(
                                                painterResource(world.getFlag()),
                                                "flag",
                                                modifier = Modifier.background(Colors.surfaceDark.value).fillMaxSize()
                                            )
                                        }
                                    }
                                    Spacer(Modifier.width(5.dp).fillMaxHeight().background(Colors.surfaceDark.value))
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Text(world.region, modifier = Modifier.align(Alignment.Center), color = Color.White)
                                    }
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}