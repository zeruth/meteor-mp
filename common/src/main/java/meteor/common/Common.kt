package meteor.common

import com.google.gson.GsonBuilder
import org.rationalityfrontline.kevent.KEVENT
import kotlin.properties.Delegates

object Common {
    /**
     * Must be initialized before rs2 and some platform specific stuff, best to do it immediately
     */
    var isAndroid by Delegates.notNull<Boolean>()
    
    val gson = GsonBuilder().setPrettyPrinting().create()
    val eventbus = KEVENT
}