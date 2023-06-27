import de.rehatech.homeekt.Homee
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

suspend fun main()
{
    val homee: Homee = Homee("URL","user", "password", device = "ApiTest")
    homee.getAccessToken()
    homee.run()
    homee.getallNodes()
    runBlocking { launch {
        homee.sendNodeBefehl(2,29,1F)
        delay(10000L)
        homee.sendNodeBefehl(2,29,126000F)
    } }.join()
    println("Fertig")

}