package meteor.common.ui.components.sidebar.buttons

class World(val id: Int, val region: String, val address: String, val portOffset: Int, val members: Boolean) {
    companion object {
        val worlds = ArrayList<World>()
        init {
            worlds.add(World(1, "United States", "https://w1.225.2004scape.org", 0, false))
            worlds.add(World(2, "United States", "https://w2.225.2004scape.org", 3, false))
            worlds.add(World(3, "Germany", "https://w3.225.2004scape.org:8443", 0, false))
            worlds.add(World(4, "Germany", "https://w4.225.2004scape.org:8443", 3, false))
            worlds.add(World(5, "Russia", "https://w5.2004.lostcity.rs", 0, false))
            worlds.add(World(6, "Russia", "https://w5.2004.lostcity.rs", 3, false))
        }

        fun World.getFlag(): String {
            if (this.region == "United States") {
                return "flags/us.png"
            }
            if (this.region == "Germany") {
                return "flags/de.png"
            }
            if (this.region == "Australia") {
                return "flags/au.png"
            }
            if (this.region == "Japan") {
                return "flags/jp.png"
            }
            if (this.region == "Russia") {
                return "flags/rs.png"
            }
            return "flags/us.png"
        }
    }
}