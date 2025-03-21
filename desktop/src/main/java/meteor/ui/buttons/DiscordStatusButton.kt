package meteor.ui.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Discord
import meteor.discord.DiscordPresence
import meteor.discord.DiscordPresence.updatingDiscordState
import meteor.common.config.ConfigManager
import meteor.common.ui.components.sidebar.SidebarButton

class DiscordStatusButton : SidebarButton(
    icon = LineAwesomeIcons.Discord,
    actionButton = true) {

    companion object {
        var state = mutableStateOf("")

        @Suppress("DEPRECATION")
        @Composable
        fun showDiscordStatusWindow() {
            val focusRequester = remember { FocusRequester() }
            state.value = ConfigManager.get<String>("DiscordRPCStatus", "")
            Window(onCloseRequest = { updatingDiscordState.value = false },
                title = "Update Discord status",
                state = WindowState(placement = WindowPlacement.Floating, position = WindowPosition.Aligned(Alignment.Center), size = DpSize(800.dp, 150.dp)),
                undecorated = false,
                resizable = false,
                alwaysOnTop = true,
                icon = painterResource("Meteor.ico")
            ) {
                Box(Modifier.fillMaxSize().background(meteor.common.ui.Colors.surfaceColor)) {
                    Row(Modifier.fillMaxSize()) {
                        Column(Modifier.fillMaxSize()) {
                            TextField(
                                state.value, {
                                state.value = it
                            }, maxLines = 1, modifier = Modifier.fillMaxWidth().height(60.dp).focusRequester(focusRequester), colors = TextFieldDefaults.textFieldColors(textColor = meteor.common.ui.Colors.secondary.value, cursorColor = meteor.common.ui.Colors.secondary.value, focusedIndicatorColor = meteor.common.ui.Colors.secondary.value), keyboardActions = KeyboardActions(
                                onDone = {
                                    confirm()
                                }
                            ),  keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ))
                            Button(onClick = {
                                confirm()
                            }, colors = ButtonDefaults.buttonColors(backgroundColor = meteor.common.ui.Colors.secondary.value),
                                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                Text("Confirm", color = Color.Black)
                            }
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        }

        fun confirm() {
            updatingDiscordState.value = false
            sendUpdate()
        }

        fun sendUpdate() {
            DiscordPresence.update(state.value)
            ConfigManager.set("DiscordRPCStatus", state.value)
        }
    }

    override fun onClick() {
        updatingDiscordState.value = !updatingDiscordState.value
    }
}