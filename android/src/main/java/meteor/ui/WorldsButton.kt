package meteor.ui

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
import client.client.nodeId
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.GlobeSolid
import compose.icons.lineawesomeicons.StarSolid
import jagex2.client.Configuration
import jagex2.client.WebSocketProxy.REMOTE_WSS
import meteor.common.ui.Colors
import meteor.common.ui.components.panel.PanelComposables
import meteor.common.ui.components.sidebar.SidebarButton
import meteor.common.ui.components.worlds.World
import meteor.common.ui.components.worlds.WorldsCommon
import meteor.common.ui.components.worlds.WorldsCommon.currentWorld

class WorldsButton : SidebarButton(icon = LineAwesomeIcons.GlobeSolid) {
    override fun onClick() {
        PanelComposables.content.value = WorldsList()
    }

    companion object {
        fun World.getAndroidFlag(): Int {
            if (this.region == "United States") {
                return com.meteor.android.R.drawable.flag_us
            }

            if (this.region == "Australia") {
                return com.meteor.android.R.drawable.flag_au
            }

            if (this.region == "Russia") {
                return com.meteor.android.R.drawable.flag_ru
            }

            if (this.region == "India") {
                return com.meteor.android.R.drawable.flag_in
            }

            if (this.region == "Sweden") {
                return com.meteor.android.R.drawable.flag_sw
            }

            return com.meteor.android.R.drawable.flag_au
        }

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
                            for (world in WorldsCommon.worlds) {
                                Row(
                                    Modifier.height(20.dp)
                                        .background(if (currentWorld.intValue == world.id) Colors.surfaceDarker.value else Colors.surface.value)
                                        .clickable {
                                            currentWorld.intValue = world.id
                                            nodeId = 9 + world.id // Friends list depends on this for local player world #
                                            client.client.members = world.members
                                            Configuration.updatePortOffset(world.portOffset)
                                            Configuration.updateModulus(world.modulus)
                                            REMOTE_WSS = world.url
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
                                    Spacer(Modifier.width(5.dp).fillMaxHeight().background(Colors.surfaceDark.value))
                                    Box(modifier = Modifier.background(Colors.surfaceDark.value).fillMaxWidth(.15f)) {
                                        Image(
                                            painterResource(world.getAndroidFlag()),
                                            "flag",
                                            modifier = Modifier.background(Colors.surfaceDark.value).fillMaxSize()
                                        )
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