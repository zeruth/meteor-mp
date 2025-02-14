package meteor.platform.desktop.ui.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import meteor.platform.common.Common
import meteor.platform.common.ui.Colors
import meteor.platform.common.ui.components.panel.PanelComposables
import meteor.platform.common.ui.components.sidebar.SidebarButton
import meteor.platform.common.ui.worlds.World
import meteor.platform.common.ui.worlds.WorldsCommon
import meteor.platform.common.ui.worlds.WorldsCommon.currentWorld

class WorldsButton : SidebarButton(icon = mutableStateOf(LineAwesomeIcons.GlobeSolid)) {
    override fun onClick() {
        PanelComposables.content.value = WorldsList()
    }

    companion object {
        fun World.getDesktopFlag(): String {
            if (this.region == "United States") {
                return "flags/flag_us.png"
            }
            if (this.region == "Germany") {
                return "flags/flag_de.png"
            }
            if (this.region == "Australia") {
                return "flags/flag_au.png"
            }
            if (this.region == "Japan") {
                return "flags/flag_jp.png"
            }
            if (this.region == "Russia") {
                return "flags/flag_ru.png"
            }
            if (this.region == "India") {
                return "flags/flag_in.png"
            }
            if (this.region == "Sweden") {
                return "flags/flag_sw.png"
            }
            return "flags/flag_us.png"
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
                                        .background(if (currentWorld.value == world.id) Colors.surfaceDarker.value else Colors.surface.value)
                                        .clickable {
                                            currentWorld.value = world.id
                                            nodeId = 9 + world.id
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
                                    if (!Common.isAndroid) {
                                        Spacer(Modifier.width(5.dp).fillMaxHeight().background(
                                            Colors.surfaceDark.value))
                                        Box(modifier = Modifier.background(Colors.surfaceDark.value).fillMaxWidth(.15f)) {
                                            Image(
                                                painterResource(world.getDesktopFlag()),
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