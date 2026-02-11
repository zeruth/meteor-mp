package meteor.platform

import jagex3.client.GameShell
import jagex3.javconfig.JavConfigParameter
import jagex3.javconfig.ModeWhat
import meteor.platform.android.AndroidContext
import java.net.URL

object ContextCommon {
    var host = if (GameShell.context is AndroidContext) "10.0.2.2" else "127.0.0.1"
    fun getCodeBase(): URL {
        try {
            return URL("http://$host:7001");
        } catch (_: Exception) {
            throw RuntimeException("Could not get valid host");
        }
    }

    fun getParameter(name: String): String? {
        when (name) {
            JavConfigParameter.MODEWHAT.id -> {
                return ModeWhat.WIP.id.toString()
            }
            JavConfigParameter.MODEWHERE.id -> {
                // todo: modewhere enum?
                return "2"
            }
            JavConfigParameter.MEMBERS.id -> {
                return "true"
            }
            JavConfigParameter.WORLDLIST_URL.id -> {
                return "http://$host:7001/slr.ws?order=LPWM"
            }
            else -> return null
        }
    }
}