package meteor.platform.android

import meteor.platform.common.Common.eventbus
import net.runelite.api.Callbacks

object Hooks : Callbacks {
    override fun post(event: Any) {
        eventbus.post(event)
    }
}