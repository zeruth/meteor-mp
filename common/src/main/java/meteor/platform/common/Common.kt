package meteor.platform.common

import androidx.compose.runtime.mutableStateOf
import com.google.gson.GsonBuilder
import meteor.logger.Logger
import net.runelite.api.events.PreTooltip
import org.rationalityfrontline.kevent.KEVENT
import kotlin.properties.Delegates

object Common {
    /**
     * Must be initialized before rs2 and some platform specific stuff, best to do it immediately
     */
    var isAndroid by Delegates.notNull<Boolean>()
    val startupTime = System.currentTimeMillis()
    lateinit var clientInstance: net.runelite.api.Client
    val gson = GsonBuilder().setPrettyPrinting().create()
    val eventbus = KEVENT
    val logger = Logger()

    init {
        eventbus.subscribe<PreTooltip> {
            if (clientInstance.inGame()) {
                if (clientInstance.shiftPressed) {
                    val newOptions = arrayListOf<String?>()
                    val newActions = arrayListOf<Int?>()
                    val newParamAs = arrayListOf<Int?>()
                    val newParamBs = arrayListOf<Int?>()
                    val newParamCs = arrayListOf<Int?>()

                    var targetIndex = -1
                    for ((i, option) in clientInstance.menuOptions.withIndex()) {
                        if (option == null)
                            break
                        if (option.startsWith("Drop ")) {
                            targetIndex = i
                            break
                        }
                    }
                    if (targetIndex != -1) {
                        for (i in 0..clientInstance.menuSize) {
                            if (i != targetIndex) {
                                newOptions.add(clientInstance.menuOptions[i])
                                newActions.add(clientInstance.menuActions[i])
                                newParamAs.add(clientInstance.menuParamAs[i])
                                newParamBs.add(clientInstance.menuParamBs[i])
                                newParamCs.add(clientInstance.menuParamCs[i])
                            }
                        }
                        newOptions.add(clientInstance.menuOptions[targetIndex])
                        newActions.add(clientInstance.menuActions[targetIndex])
                        newParamAs.add(clientInstance.menuParamAs[targetIndex])
                        newParamBs.add(clientInstance.menuParamBs[targetIndex])
                        newParamCs.add(clientInstance.menuParamCs[targetIndex])

                        for (i in 0..<newOptions.size) {
                            clientInstance.menuOptions[i] = newOptions[i]
                            clientInstance.menuActions[i] = newActions[i] ?: -1
                            clientInstance.menuParamAs[i] = newParamAs[i] ?: -1
                            clientInstance.menuParamBs[i] = newParamBs[i] ?: -1
                            clientInstance.menuParamCs[i] = newParamCs[i] ?: -1
                        }
                    }
                }
            }
        }
    }
}