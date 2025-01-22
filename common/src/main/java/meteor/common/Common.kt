package meteor.common

import com.google.gson.GsonBuilder
import org.rationalityfrontline.kevent.KEVENT
import kotlin.properties.Delegates

object Common {
    var isAndroid by Delegates.notNull<Boolean>()
    val gson = GsonBuilder().setPrettyPrinting().create()
    val eventbus = KEVENT
}