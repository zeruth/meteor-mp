package meteor

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import net.runelite.api.Client

object Main {
    var forceRecomposition = mutableStateOf(false)
    lateinit var client: Client
    var composeTime = mutableLongStateOf(0L)
    var swingTime = mutableLongStateOf(0L)
    val version = "1.0.4-b4"
    val interfaceOpen = mutableStateOf(false)
}