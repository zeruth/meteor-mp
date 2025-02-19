import jagex2.client.GameShellMapView
import org.rationalityfrontline.kevent.KEVENT
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JFrame

class BufferedFrame(mapview: mapview?) : JFrame() {
    var mapWidth = 635
    var mapHeight = 503
    init {
/*        size = Dimension(mapWidth, mapHeight)
        isUndecorated = true
        isVisible = true
        addKeyListener(mapview)
        addMouseListener(mapview)
        addMouseMotionListener(mapview)
        addWindowListener(mapview)

        KEVENT.subscribe<MapViewUpdate> {
            paint(graphics)
        }*/
    }

/*    override fun paint(g: Graphics) {
        g.drawImage(GameShellMapView.image, 0, 0, this)
    }

    override fun update(g: Graphics) {
        g.drawImage(GameShellMapView.image, 0, 0, this)
    }*/
}
