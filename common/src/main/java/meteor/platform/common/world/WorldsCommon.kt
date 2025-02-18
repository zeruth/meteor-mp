package meteor.platform.common.world

import androidx.compose.runtime.mutableIntStateOf
import java.math.BigInteger

object WorldsCommon {
    val worlds = ArrayList<World>()
    val keys = HashMap<Int, String>()
    var currentWorld = mutableIntStateOf(1)

    init {
        keys[1] = "139166108114809420209531572233728681862766242438406812485014398252909792593544769137940557006863538599636274554651584456254675556785083138887179976927719905275878548783350744119907089781350385509570239196626810083985967569509813157660824321253196703334469936154617584647589437553301933388243942245339112244547"
        keys[2] = "160131955148921835578395499889549080668987477270392128927654973103519654871965860030652620917514122396339231761307213190440927404588176857404161799716863444105250884954039521847151937450670895865779077203838773075289471661310356507698669119090386441245722656333943027855567603507873503955505180911383681112411"
        keys[3] = "157603847053086735313954561871176786586738514698243239998774874078124979095094527217871626584013583693065526164459574902807282597820183391784102943107128050379973057949325948492150133682329996479004156696739261234210499833533310095924616713600250152781015764934327011706924269865382011681205780274779507552069"
        keys[4] = "167364114448287474025547348375331324043877736973215209150246329563828945590629571146458624397510753229667673826182971274980861168353931639415116964835345410851683535907919794725170159915649077182692605209305177608710161124184073030567539839046413883255339336454259630676378188937198789017609005699976223459903"
        keys[5] = "178323269370683540176031748054947606096074034289467819046044453240738617343085922917832475516889703923245740249838365740742862400535051446197744184251747178246061803825998637454515296321385127635448969806415012021949475529332410474369684122982373701868764346967107765063294261346836420912320439843240013094617"
        keys[6] = "159485481540212678704561082906641953114340640297385106038913593785675446535419631604434474716794786883421105250577421335372595550655358191590432248086085884428794051110472771855367089096420834400133251976105395855415972467396233410308374954402925677480387121175453953888611137275790713488050831121479975560613"
        keys[7] = "137984608868359180262152879754781578335451668944433735933087450677157158030825064320701814402731760498951454110287270778261478258840341034730604297299316246721822196750502196841900883251792102223880909969099541226550275621255712535308183117084139184328605405386852101987136586209636810717481364143913517381887"
        keys[8] = "127441148241832514318312057198824892696479983557442584365969762763014216718341698061024309502538957331394692845924595247848265288867522069965721929848194970071851155759020947708940206985452549069435658309433718746981020987936942420534106166754804383271059360593813141898412720357595786254744513710930473070523"
        keys[9] = "154446257489838622041186258292018879953738978868275117180748527263059544791160863841446211933813721414112338422549966293585100381553770299945065526180243510812877939149224311431160461027871214726469855392181730974994152036327821051287670457771019724777283428259306251778933797004303634032618705183503762870959"
        keys[10] = "153820896830881231738167735191518257986047317403975109538334719241597207488874455568888256810497596337262095999655284722980361433775548839866683218553316940363886475656421285182674915176559694813373647906025415532611685557853185081754118035403199466572346404870018976663095510061104743407643497456235516230309"

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