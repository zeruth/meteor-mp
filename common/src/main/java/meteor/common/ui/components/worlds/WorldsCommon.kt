package meteor.common.ui.components.worlds

import java.math.BigInteger

object WorldsCommon {
    val worlds = ArrayList<World>()
    val keys = HashMap<Int, String>()

    init {
        keys[1] = "99393576224256542909725185182269692719761471521832032637095318489154116124041038817505460846735298479802573420331847527479493187768841906404663869827406659934003942531638921538754165197505828228820167436197369284546310245220155070665238828070261833565245607125379419288636945550419881275516000914512066675997"
        keys[2] = "125072346721424022370276934176407096097990248389842967457299107441556178017865977990710808492299733195641695141529751199629434586671522678647160689209393777463632633461567490351157388537866118735293981491465467094213959374004545139122937715193359729046393997965463452929188248444548401810349398836411545102171"
        keys[3] = "112576263139365737893619669565737760256579784168628021553523224816829200044205629306805306966160753388170869535303458649826596437099237313976109149985466099150287889735462155900838513981139604003713120664507671826237281963342083047806045793225032270750176758146813149098426792793376717034365250984597569643247"
        keys[4] = "149479177879709308185366688283078564489747783057560908242454436949118514232793800804649021381942893886564586822626797181899194064549046897538017039791866904364786132366670609707628388690949115683767591980668142819215925255655837893045533872957341164360204156529924741938124586021580068852049835236903166672573"
        keys[5] = "121331991357321402805812061194744539084977566135393831334916598978245414562433745897429968858241213481732555118992329777031144901071556756555548727902654605232334617958572965116085401209836612107379124233754272558305291111275512698949451861459681443942844668905998054671022238166511954337037470476917105421227"
        keys[6] = "140978960566587853718568414173917072838497259305179377451721115668737659620206795112305229655978584015480156591041822348134715848302794425428391156899513955330112809001997319503252499203532376486601878665699561741405008320171491378098570485945141458338003967376754884397206086966852040524976123284725811497369"
        keys[7] = "121326358279794638604835328179445952459345256003865249603786782894176043456718218119779932644445862067960285873098406146782120342119451703519749203492571806234781622873470429309848238283728482254228382529356377906178054349140417303295216426971453855755352302099928091380368057078311385851012341205828654047979"
        keys[8] = "112212874311463593970929696710555545004535516580296654022722398775125754961896368654280026796469775243682310717758476437067769659734157783994176459501418503457905689976974753760676166618686174957346348451029215122427662062757506328532517707361085257163624104818027713802251884429772725076750712432795971504177"
        keys[9] = "159998482437517209028048675340806485360744849516201611805080662497506022692869952038462942710286374784513762850168648849046452107599818129033584806797272504369918678605829301712681920704334478926737982868837764854984261099783011493934265043904670922453323258246704698675779317418519682242207545172140848148419"
        keys[10] = "138001423002576040665380064380572055242282382595367507601596620530925136247774200038976407773881764805931693472190240885537242132571606618697169544394153856255477710647975627698561454842472988231398236265542193726358919110400728127215823499802713843234329287949622035008374520789520639054189923727577537181369"

        worlds.add(World(1, "United States",  0, false, "wss://w1-2004.lostcity.rs", BigInteger(keys[1]!!)))
        worlds.add(World(2, "United States",  3, false, "wss://w2-2004.lostcity.rs", BigInteger(keys[2]!!)))
        worlds.add(World(3, "Sweden",  0, false, "wss://w3-2004.lostcity.rs",  BigInteger(keys[3]!!)))
        worlds.add(World(4, "Sweden",  3, false, "wss://w4-2004.lostcity.rs",  BigInteger(keys[4]!!)))
        worlds.add(World(5, "Russia",  0, false, "wss://w5-2004.lostcity.rs",  BigInteger(keys[5]!!)))
        worlds.add(World(6, "Russia",  3, false,  "wss://w6-2004.lostcity.rs", BigInteger(keys[6]!!)))
        worlds.add(World(7, "India",  0, false, "wss://w7-2004.lostcity.rs",  BigInteger(keys[7]!!)))
        worlds.add(World(8, "India",  3, false, "wss://w8-2004.lostcity.rs",  BigInteger(keys[8]!!)))
        worlds.add(World(9, "Australia",  0, false, "wss://w9-2004.lostcity.rs",  BigInteger(keys[9]!!)))
        worlds.add(World(10, "Australia",  3, false, "wss://w10-2004.lostcity.rs",  BigInteger(keys[10]!!)))
    }
}