package meteor.ui.components.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import meteor.common.config.Config
import meteor.common.config.ConfigItem
import meteor.common.plugin.PluginManager
import meteor.common.plugin.meteor.MeteorPlugin
import meteor.common.plugin.meteor.UIColor
import meteor.common.ui.Colors
import meteor.ui.components.sidebar.buttons.PluginsButton.Companion.switchStateMap
import meteor.ui.components.sidebar.buttons.PluginsButton.Companion.textStateMap

object ConfigComposables {

    @Suppress("UNCHECKED_CAST")
    @Composable
    fun ConfigPanel(config: Config) {
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
                                ConfigNode(height = 35) { EnumConfigNode(item as ConfigItem<Enum<meteor.plugin.meteor.FilterQuality>>).invoke(this) }
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
                uncheckedThumbColor = Colors.surfaceDark.value,
                checkedThumbColor = Colors.secondary.value
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
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Colors.secondary.value)
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
            Button(onClick = { expanded = true }, colors = buttonColors(backgroundColor = Colors.surfaceDark.value)) {
                Text(text = selectedOption.name, style = TextStyle(color = Colors.secondary.value))
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Colors.surfaceDark.value)
            ) {
                getEnumEntries<T>().forEach { option ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        selectedOption = option
                        config.set(selectedOption)
                        println("should have set")
                    }, modifier = Modifier.background(Colors.surfaceDark.value)) {
                        if (T::class == UIColor::class) {
                            Text(text = option.name, color = (option as UIColor).color)
                        }
                        else {
                            Text(text = option.name, color = Colors.secondary.value)
                        }
                    }
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
            colors = TextFieldDefaults.textFieldColors(
                textColor = Colors.secondary.value,
                backgroundColor = Colors.surface.value,
                focusedIndicatorColor = Colors.secondary.value,
                unfocusedIndicatorColor = Colors.secondary.value
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}