package meteor.platform.android.ui.map

import MapViewUpdate
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import client.events.DrawFinished
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.WindowCloseSolid
import ext.awt.BufferedImageExt.getPixels
import jagex2.client.GameShellMapView
import mapview
import meteor.platform.common.ui.Colors
import org.rationalityfrontline.kevent.KEVENT

object MapView {
    val mapVisible = mutableStateOf(false)
    val mapLoaded = mutableStateOf(false)
    var mapPendingStart = false
    lateinit var mapView: mapview
    init {
        KEVENT.subscribe<DrawFinished> {
            if (mapPendingStart) {
                mapPendingStart = false
                mapView = mapview()
                mapView.init()
                mapView.load()
                mapLoaded.value = true
            }
        }
        KEVENT.subscribe<MapViewUpdate> {
            GameShellMapView.image?.let {
                mapviewImage.value = Bitmap.createBitmap(it.getPixels(), it.width, it.height, Bitmap.Config.RGB_565).asImageBitmap()
            }
        }
    }

    var mapScaleX = 0f
    var mapScaleY = 0f

    val undecorated = mutableStateOf(true)
    val focusRequester = FocusRequester()
    val mapviewImage = mutableStateOf<ImageBitmap?>(null)

    @Composable
    fun MapView() {
        key(mapVisible.value) {
            key(mapLoaded.value) {
                if (mapVisible.value && !mapLoaded.value)
                    mapPendingStart = true
                if (mapVisible.value && mapLoaded.value) {
                    Box(modifier = Modifier.focusable().fillMaxSize().focusRequester(focusRequester).registerDragListener().registerLeftClickListener().registerMouseMoveListener().onSizeChanged {
                        mapScaleX = (635f / it.width)
                        mapScaleY = (503f / it.height)
                    }) {
                        mapviewImage.value?.let {
                            Image(it, "map", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
                        }
                        Image(LineAwesomeIcons.WindowCloseSolid, "Exit", colorFilter = ColorFilter.tint(
                            Color.Red), modifier = Modifier.size(50.dp).background(Colors.surface.value).clip(RoundedCornerShape(10.dp)).align(Alignment.TopEnd).clickable {
                            mapVisible.value = false
                        })
                    }
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                }
            }
        }
    }

    fun Modifier.registerDragListener(): Modifier {
        return this.pointerInput(Unit) {
            detectDragGestures(onDragStart = {
                sendLeftClick(it.x.toInt(), it.y.toInt(), false)
            }, onDragCancel = {
                mapView.mouseReleased(false)
            }, onDragEnd = {
                mapView.mouseReleased(false)
            }) { change, dragAmount ->
                sendMouseMove(change.position.x.toInt(), change.position.y.toInt())
            }
        }
    }

    fun Modifier.registerLeftClickListener(): Modifier {
        return this.pointerInput(Unit) {
            detectTapGestures(onTap = { offset ->
                focusRequester.requestFocus()
                sendLeftClick(offset.x.toInt(), offset.y.toInt())
            })
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    fun Modifier.registerMouseMoveListener(): Modifier {
        return this.pointerInteropFilter { change ->
            sendMouseMove(change.x.toInt(), change.y.toInt())
            false
        }
    }

    fun sendMouseMove(x: Int, y: Int) {
        val offset = Offset(x.toFloat(), y.toFloat())
        val adjustedX = (offset.x * mapScaleX)
        val adjustedY = (offset.y * mapScaleY)
        mapView.mouseMoved(adjustedX.toInt(), adjustedY.toInt())
    }

    fun sendLeftClick(x: Int, y: Int, release: Boolean = true) {
        val adjustedX = (x * mapScaleX)
        val adjustedY = (y * mapScaleY)
        mapView.mouseMoved(adjustedX.toInt(), adjustedY.toInt())
        mapView.mousePressed(adjustedX.toInt(), adjustedY.toInt(), 1, false)
        if (release)
            mapView.mouseReleased(false)
    }
}