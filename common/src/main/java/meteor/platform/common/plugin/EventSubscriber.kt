package meteor.platform.common.plugin


import meteor.platform.common.events.ConfigChanged
import org.rationalityfrontline.kevent.KEventSubscriber
import org.rationalityfrontline.kevent.SubscriberThreadMode
import org.rationalityfrontline.kevent.subscribe
import org.rationalityfrontline.kevent.unsubscribeAll

/**
 * A utility class mainly used by plugins to subscribe to generic events
 */
open class EventSubscriber : KEventSubscriber {
    /**
     * If not listening, an event will be received but ignored and never processed.
     */
    var listening: Boolean = false

    /**
     * The child class will override open functions it wants to subscribe to
     */
    open fun onConfigChanged(it: ConfigChanged) {}

    /**
     * Here we register the Event types to their function, wrapped around a check for listening
     */
    fun subscribeEvents(startListening: Boolean) {
        subscribeEvent<ConfigChanged> { executeIfListening { onConfigChanged(it) } }
        if (startListening)
            this.listening = true
    }

    /**
     * Gatekeeper function that will prevent events from firing if you don't want them to while being subscribed
     * TODO: Implement a way to block only certain events
     */
    open fun executeIfListening(unit: () -> (Unit)) {
        if (listening)
            unit()
    }

    /**
     * Subscribe the [unit] to [T] events via [SubscriberThreadMode.POSTING] thread (almost always client thread)
     * but that could be a different thread if you fire an event off from say a composable
     */
    private inline fun <reified T : Any> subscribeEvent(noinline unit: (T) -> Unit) {
        subscribe(threadMode = SubscriberThreadMode.POSTING) { event -> unit.invoke(event.data) }
    }

    fun unsubscribe() {
        unsubscribeAll()
        listening = false
    }
}