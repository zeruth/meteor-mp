package meteor

import com.google.gson.GsonBuilder

object Main {
    val gson = GsonBuilder().setPrettyPrinting().create()
}