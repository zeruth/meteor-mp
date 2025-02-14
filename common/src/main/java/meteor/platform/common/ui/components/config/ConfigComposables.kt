package meteor.platform.common.ui.components.config

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ChevronLeftSolid
import compose.icons.lineawesomeicons.ChevronRightSolid
import meteor.platform.common.config.Config
import meteor.platform.common.config.ConfigItem
import meteor.platform.common.plugin.meteor.UIColor
import meteor.platform.common.ui.Colors
import meteor.platform.common.ui.UI.switchStateMap
import meteor.platform.common.ui.UI.textStateMap
import meteor.plugin.meteor.FilterQuality

object ConfigComposables {

    @Suppress("UNCHECKED_CAST")
    @Composable
    fun  ConfigPanel(config: Config) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                for (item in config.items) {
                    when (item.defaultValue) {
                        is Boolean -> ConfigNode { BooleanConfigNode(item as ConfigItem<Boolean>).invoke(this) }
                        is String -> ConfigNode(height = 60) { StringConfigNode(item as ConfigItem<String>).invoke(this) }
                        is Int -> ConfigNode(height = 60) { IntConfigNode(item as ConfigItem<Int>).invoke(this) }
                        is Enum<*> -> {
                            if (item.defaultValue is UIColor)
                                ConfigNode(height = 35) { EnumConfigNode(item as ConfigItem<Enum<UIColor>>).invoke(this) }
                            else if (item.defaultValue is meteor.plugin.meteor.FilterQuality)
                                ConfigNode(height = 35) { EnumConfigNode(item as ConfigItem<Enum<FilterQuality>>).invoke(this) }
                        }
                    }
                    Spacer(Modifier.height(2.dp))
                }
            }
        }
    }

    @Composable
    fun ConfigNode(height: Int = 30, content: @Composable RowScope.() -> Unit) {
        Box(Modifier.background(Colors.surface.value).fillMaxWidth().height(height.dp)) {
            Row(Modifier.height(height.dp)) {
                content.invoke(this)
            }
        }
    }

    fun BooleanConfigNode(config: ConfigItem<Boolean>) : @Composable RowScope.() -> Unit = @Composable {
        switchStateMap.putIfAbsent(config.key, config.get())
        Spacer(Modifier.width(5.dp))
        Text(
            config.name, Modifier.align(Alignment.CenterVertically),
            style = TextStyle(color = Colors.secondary.value, fontSize = 18.sp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            switchStateMap[config.key]!!,
            modifier = Modifier.align(Alignment.CenterVertically),
            onCheckedChange = {
                config.toggle()
                switchStateMap[config.key] = config.get()
            },
            colors = SwitchDefaults.colors(
                uncheckedTrackColor = Colors.surfaceDark.value,
                checkedTrackColor = Colors.surfaceDark.value,
                uncheckedThumbColor = Colors.secondary.value,
                checkedThumbColor = Colors.secondary.value,
                uncheckedBorderColor = Colors.surfaceDark.value,
                checkedBorderColor = Colors.surfaceDark.value
            )
        )
    }

    fun StringConfigNode(config: ConfigItem<String>) : @Composable RowScope.() -> Unit = @Composable {
        val state = textStateMap[config.key]
        val value = config.getStringValue()
        if (state == null) {
            textStateMap[config.key] = value
        }
        OutlinedTextField(
            value = textStateMap[config.key].toString(),
            visualTransformation = if (!config.secret) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = {
                config.set(it)
                textStateMap[config.key] = it
            },
            label = {
                Text(
                    config.name,
                    style = TextStyle(color = Colors.secondary.value, fontSize = 18.sp)
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxSize(),
            textStyle = TextStyle(
                color = Colors.secondary.value,
                fontSize = 16.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Colors.secondary.value,
                focusedBorderColor = Colors.secondary.value,
                unfocusedBorderColor = Colors.secondary.value)
        )
    }

    inline fun <reified T : Enum<T>> getEnumEntries(): Array<T> {
        return enumValues<T>()
    }

    inline fun<reified T : Enum<T>> EnumConfigNode(config: ConfigItem<out Enum<T>>): @Composable RowScope.() -> Unit = @Composable {
        val state = textStateMap[config.key]
        val value = config.defaultValue.name
        if (state == null) {
            textStateMap[config.key] = value
        }
        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf(config.get<T>()) }
        Row (Modifier.fillMaxSize()) {
            Spacer(Modifier.width(5.dp))
            Text(
                text = config.name,
                modifier = Modifier.align(Alignment.CenterVertically),
                style = TextStyle(color = Colors.secondary.value, fontSize = 18.sp)
            )
            Spacer(Modifier.weight(1f))
            val first = getEnumEntries<T>().first()
            val last = getEnumEntries<T>().last()
            val hasPrev = selectedOption != first
            val hasNext = selectedOption != last
            if (hasPrev) {
                Box(Modifier.size(20.dp).align(Alignment.CenterVertically).clip(RoundedCornerShape(5.dp)).clickable {
                    var nextIsPrev = false
                    getEnumEntries<T>().reversed().forEach {
                        if (it == selectedOption) {
                            nextIsPrev = true
                        }
                        else if (nextIsPrev) {
                            selectedOption = it
                            expanded = false
                            config.set(selectedOption)
                            return@clickable
                        }
                    }
                }) {
                    Image(LineAwesomeIcons.ChevronLeftSolid, "", colorFilter = ColorFilter.tint(
                        Colors.secondary.value))
                }
            }
            Button(onClick = { expanded = true }, colors = buttonColors(containerColor = Colors.surfaceDark.value)) {
                Text(text = selectedOption.name, style = TextStyle(color = Colors.secondary.value))
            }
            if (hasNext) {
                Box(Modifier.size(20.dp).align(Alignment.CenterVertically).clip(RoundedCornerShape(5.dp)).clickable {
                    var nextIsNext = false
                    getEnumEntries<T>().forEach {
                        if (it == selectedOption) {
                            nextIsNext = true
                        }
                        else if (nextIsNext) {
                            selectedOption = it
                            expanded = false
                            config.set(selectedOption)
                            return@clickable
                        }
                    }
                }) {
                    Image(LineAwesomeIcons.ChevronRightSolid, "", colorFilter = ColorFilter.tint(
                        Colors.secondary.value))
                }
            }
        }
    }


    fun IntConfigNode(config: ConfigItem<Int>) : @Composable RowScope.() -> Unit = @Composable {
        val state = textStateMap[config.key]
        val value = config.getStringValue()
        if (state == null) {
            textStateMap[config.key] = value
        }

        Spacer(Modifier.width(5.dp))
        Text(
            text = config.name,
            modifier = Modifier.align(Alignment.CenterVertically),
            style = TextStyle(color = Colors.secondary.value, fontSize = 18.sp)
        )
        Spacer(modifier = Modifier.weight(1f))
        TextField(
            value = value,
            onValueChange = { newValue ->
                // Sanitize input: Allow only valid integer input
                val sanitizedValue = newValue.filter { it.isDigit() || it == '-' }.toIntOrNull()
                if (sanitizedValue != null) {
                    config.set(sanitizedValue)
                    textStateMap[config.key] = newValue
                } else {
                    config.set(config.defaultValue)
                    textStateMap[config.key] = config.defaultValue.toString()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(200.dp),
            textStyle = TextStyle(
                color = Colors.secondary.value,
                fontSize = 18.sp
            ),
            colors = TextFieldDefaults.colors(
                cursorColor = Colors.secondary.value,
                disabledTextColor = Colors.secondary.value,
                focusedContainerColor = Colors.surfaceDark.value,
                unfocusedContainerColor = Colors.surfaceDark.value,
                focusedLabelColor = Colors.secondary.value,
                focusedIndicatorColor = Colors.secondary.value,
                unfocusedIndicatorColor = Colors.secondary.value,
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}