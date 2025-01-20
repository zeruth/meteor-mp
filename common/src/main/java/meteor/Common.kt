package meteor

import com.google.gson.GsonBuilder
import kotlin.properties.Delegates

object Common {
    var isAndroid by Delegates.notNull<Boolean>()
    val gson = GsonBuilder().setPrettyPrinting().create()
}