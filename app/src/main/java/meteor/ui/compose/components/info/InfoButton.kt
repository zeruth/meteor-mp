package meteor.ui.compose.components.info

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.meteor.android.MainActivity
import com.meteor.android.R
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.InfoCircleSolid
import meteor.Main
import meteor.Main.version
import meteor.ui.compose.Colors
import meteor.ui.compose.Colors.surface
import meteor.ui.compose.components.GeneralComposables.SidedNode
import meteor.ui.compose.components.panel.PanelComposables
import meteor.ui.compose.overlay.ViewportOverlayRoot
import meteor.ui.compose.components.sidebar.SidebarButton
import java.time.Instant
import java.util.concurrent.ConcurrentLinkedQueue

class InfoButton : SidebarButton(icon = LineAwesomeIcons.InfoCircleSolid, bottom = true) {
    private val renderTimes = ConcurrentLinkedQueue<Pair<Instant, Long>>()

    override fun onClick() {
        PanelComposables.content.value = InfoPanel()
    }

    fun InfoPanel() = @Composable {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            BubbleBoxColumn {
                Image(
                    painterResource(R.drawable.badge),
                    null,
                    modifier = Modifier.size(150.dp).align(Alignment.CenterHorizontally)
                )
            }
            Spacer(Modifier.height(2.dp))
            SidedNode(30,
                left = @Composable {
                    Spacer(Modifier.width(4.dp))
                    Text("Meteor", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                },
                right = @Composable {
                    Text("225-$version", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(Modifier.width(4.dp))
                })
            Spacer(Modifier.height(2.dp))
            SidedNode(30,
                left = @Composable {
                    Spacer(Modifier.width(4.dp))
                    Text("Injector", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                },
                right = @Composable {
                    Text("1.4", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(Modifier.width(4.dp))
                })
            Spacer(Modifier.height(2.dp))
            SidedNode(30,
                left = @Composable {
                    Spacer(Modifier.width(4.dp))
                    Text("Logger", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                },
                right = @Composable {
                    Text("1.2", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(Modifier.width(4.dp))
                })
            Spacer(Modifier.height(2.dp))
            SidedNode(30,
                left = @Composable {
                    Spacer(Modifier.width(4.dp))
                    Text("Eventbus", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                },
                right = @Composable {
                    Text("1.1", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(Modifier.width(4.dp))
                })
            Spacer(Modifier.height(16.dp))
            SidedNode(30,
                left = @Composable {
                    Spacer(Modifier.width(4.dp))
                    Text("OS", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                },
                right = @Composable {
                    Text(
                        "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
                        color = Colors.secondary.value,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.width(4.dp))
                })
            Spacer(Modifier.height(2.dp))
            SidedNode(30,
                left = @Composable {
                    Spacer(Modifier.width(4.dp))
                    Text("Compose-UI", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                },
                right = @Composable {
                    Text(
                        "${Main.composeTime.value}ms",
                        color = Colors.secondary.value,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.width(4.dp))
                })
            Spacer(Modifier.height(2.dp))
            SidedNode(30,
                left = @Composable {
                    Spacer(Modifier.width(4.dp))
                    Text("Compose-Canvas", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                },
                right = @Composable {
                    Text(
                        "${ViewportOverlayRoot.canvasRenderTime.value}ms",
                        color = Colors.secondary.value,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.width(4.dp))
                })

            val renderTime = (ViewportOverlayRoot.canvasRenderTime.value + Main.composeTime.value + Main.swingTime.value).coerceAtLeast(1)
            val now = Instant.now()
            renderTimes.add(now to renderTime)
            removeOldEntries()

            Spacer(Modifier.height(2.dp))
            SidedNode(30,
                left = @Composable {
                    Spacer(Modifier.width(4.dp))
                    Text("AWT-Game (1sec Avg)", color = Colors.secondary.value, modifier = Modifier.align(Alignment.CenterVertically))
                },
                right = @Composable {
                    Text(
                        "${MainActivity.fps.intValue} fps",
                        color = Colors.secondary.value,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.width(4.dp))
                })
        }
    }

    private fun removeOldEntries() {
        val cutoff = Instant.now().minusSeconds(1)
        while (renderTimes.peek()?.first?.isBefore(cutoff) == true) {
            renderTimes.poll()
        }
    }

    @Composable
    private fun ColumnScope.BubbleBoxColumn(content: @Composable () -> Unit) {
        Box(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(surface.value)
                .align(Alignment.CenterHorizontally)
        ) {
            Column {
                content.invoke()
            }
        }
    }

    @Composable
    private fun ColumnScope.BubbleBoxRow(content: @Composable () -> Unit) {
        Box(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(surface.value)
                .align(Alignment.CenterHorizontally)
        ) {
            Row {
                content.invoke()
            }
        }
    }

    @Composable
    private fun ColumnScope.BubbleBoxCentered(content: @Composable () -> Unit) {
        BubbleBoxColumn {
            BubbleBoxRow {
                content.invoke()
            }
        }
    }
}