package meteor.platform.android.ui.cameracontrols

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import client.events.DrawFinished
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.*
import meteor.platform.common.Common.clientInstance
import meteor.platform.common.Common.eventbus
import meteor.platform.common.ext.kotlin.MutableStateExt.toggle
import meteor.platform.common.ui.Colors
import meteor.platform.android.ui.GamePanel.containerSize
import java.awt.event.KeyEvent

object CameraControls {

    val ingame = mutableStateOf(false)

    init {
        eventbus.subscribe<DrawFinished> {
            ingame.value = clientInstance.ingame
        }
    }

    val showControls = mutableStateOf(true)

    var leftPressed = false
    var rightPressed = false
    var downPressed = false
    var upPressed = false

    fun resetKeys() {
        if (leftPressed) {
            clientInstance.keyReleased(KeyEvent.VK_LEFT)
            leftPressed = false
        }
        if (rightPressed) {
            clientInstance.keyReleased(KeyEvent.VK_RIGHT)
            rightPressed = false
        }
        if (downPressed) {
            clientInstance.keyReleased(KeyEvent.VK_DOWN)
            downPressed = false
        }
        if (upPressed) {
            clientInstance.keyReleased(KeyEvent.VK_UP)
            upPressed = false
        }
    }

    @Composable
    fun BoxScope.CameraControls() {
        if (!ingame.value)
            return
        key(containerSize.value) {
            Box(modifier = Modifier
                .fillMaxSize()) {
                Box(modifier = Modifier.size(40.dp).align(Alignment.TopEnd).offset(y = 15.dp).pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            showControls.toggle()
                        }
                    )
                }) {
                    Image(LineAwesomeIcons.Eye, contentDescription = null, colorFilter = ColorFilter.tint(
                        Colors.secondary.value), modifier = Modifier.fillMaxSize())
                }
                if (showControls.value) {
                    Box(modifier = Modifier.size(40.dp).align(Alignment.CenterStart).pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                leftPressed = true
                                clientInstance.keyPressed(java.awt.event.KeyEvent.VK_LEFT, -1)
                            },
                            onTap = {
                                leftPressed = false
                                clientInstance.keyReleased(java.awt.event.KeyEvent.VK_LEFT)
                            }
                        )
                    }) {
                        Image(LineAwesomeIcons.ChevronLeftSolid, contentDescription = null, colorFilter = ColorFilter.tint(
                            Colors.secondary.value), modifier = Modifier.fillMaxSize())
                    }
                    Box(modifier = Modifier.size(40.dp).align(Alignment.TopCenter).pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                upPressed = true
                                clientInstance.keyPressed(java.awt.event.KeyEvent.VK_UP, -1)
                            },
                            onTap = {
                                upPressed = false
                                clientInstance.keyReleased(java.awt.event.KeyEvent.VK_UP)
                            }
                        )
                    }) {
                        Image(LineAwesomeIcons.ChevronUpSolid, contentDescription = null, colorFilter = ColorFilter.tint(
                            Colors.secondary.value), modifier = Modifier.fillMaxSize())
                    }
                    Box(modifier = Modifier.size(40.dp).align(Alignment.CenterEnd).pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                rightPressed = true
                                clientInstance.keyPressed(java.awt.event.KeyEvent.VK_RIGHT, -1)
                            },
                            onTap = {
                                rightPressed = false
                                clientInstance.keyReleased(java.awt.event.KeyEvent.VK_RIGHT)
                            }
                        )
                    }) {
                        Image(LineAwesomeIcons.ChevronRightSolid, contentDescription = null, colorFilter = ColorFilter.tint(
                            Colors.secondary.value), modifier = Modifier.fillMaxSize())
                    }
                    Box(modifier = Modifier.size(40.dp).align(Alignment.BottomCenter).pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                downPressed = true
                                clientInstance.keyPressed(java.awt.event.KeyEvent.VK_DOWN, -1)
                            },
                            onTap = {
                                downPressed = true
                                clientInstance.keyReleased(java.awt.event.KeyEvent.VK_DOWN)
                            }
                        )
                    }) {
                        Image(LineAwesomeIcons.ChevronDownSolid, contentDescription = null, colorFilter = ColorFilter.tint(
                            Colors.secondary.value), modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}