package com.meteor.android

import net.runelite.api.Callbacks
import org.rationalityfrontline.kevent.KEVENT

class Hooks : Callbacks {
    override fun post(event: Any) {
        KEVENT.post(event)
    }
}