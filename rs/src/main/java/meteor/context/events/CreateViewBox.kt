package meteor.context.events

import jagex2.client.ViewBox

class CreateViewBox(private val width: Int ,private val height: Int) {
    lateinit var viewBox: ViewBox
}