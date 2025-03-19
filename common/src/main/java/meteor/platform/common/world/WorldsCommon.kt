package meteor.platform.common.world

import androidx.compose.runtime.mutableIntStateOf
import java.math.BigInteger

object WorldsCommon {
    val worlds = ArrayList<World>()
    val keys = HashMap<Int, String>()
    var currentWorld = mutableIntStateOf(1)

    init {
        keys[1] = "169752106786710064541035805159791232051397487532463637617778432786997948649795680139871215498186236617385484173038118638870747407547587714046535687168907655678847550211401372523783638322258882899775157597426218076397967823600926909521689070976549392112474828866258865295605890491947016141456882063095248699397"
        worlds.add(World(1, "United States",  0, true, "ws://w1.04lite.com", BigInteger(keys[1]!!)))
    }
}