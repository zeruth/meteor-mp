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

    init {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe")
    }
    @JvmStatic
    fun main(args: Array<String>) {
        gatherKeys()

        for (world in rsaMap.keys) {
            println("keys[$world] = \"${rsaMap[world]}\"")
        }
    }

    fun gatherKeys() {
        val options = ChromeOptions()
        options.addArguments("--remote-allow-origins=*", "--headless", "--no-sandbox", "--disable-dev-shm-usage")
        val driver: WebDriver = ChromeDriver(options)

        for (world in 1..10) {
            val url = "https://w$world-2004.lostcity.rs/client/client.js"

            driver.get(url)
            driver.pageSource?.let {
                val document = Jsoup.parse(it)

                rsaMap[world] = document.body().text()
                    .split("\"6553")[1]
                    .split("BigInt(\"")[1]
                    .split("\"")[0]
            }
        }

        driver.quit()
    }
}
