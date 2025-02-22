import org.jsoup.Jsoup
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

/**
 * Download and install ChromeDriver that matches your chrome desktop version
 * https://googlechromelabs.github.io/chrome-for-testing/
 *
 * Put it in C:\\chromedriver-win64
 */
object RSAScraper {
    val rsaMap = HashMap<Int, String>()
    val options: ChromeOptions
    val driver: ChromeDriver
    init {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe")
        options = ChromeOptions()
        options.addArguments("--remote-allow-origins=*", "--headless", "--no-sandbox", "--disable-dev-shm-usage")
        driver = ChromeDriver(options)
    }
    @JvmStatic
    fun main(args: Array<String>) {
        gatherKeys()

        for (world in rsaMap.keys) {
            println("keys[$world] = \"${rsaMap[world]}\"")
        }
    }

    val keyMap = ArrayList<String>()

    fun gatherKey(world: Int) {
        keyMap.clear()
        val url = "https://w$world-2004.lostcity.rs/client/client.js"
        val start = System.currentTimeMillis()
        try {
            driver.get(url)
            driver.pageSource?.let {
                val document = Jsoup.parse(it)
                for (idx in document.body().text()
                    .split("BigInt(\"")) {
                    keyMap.add(idx.split("\"")[0])
                }
                val key = keyMap.sortedBy { it.length }.reversed().first()
                rsaMap[world] = key
                println("Gathered key: $world in ${System.currentTimeMillis() - start}ms")
            }
        }catch (e:Exception){
            println("Failed collecting $world key (Make sure chrome is open) retrying...")
            gatherKey(world)
        }
    }

    fun gatherKeys() {
        for (world in 1..10) {
            gatherKey(world)
        }
        driver.quit()
    }
}
