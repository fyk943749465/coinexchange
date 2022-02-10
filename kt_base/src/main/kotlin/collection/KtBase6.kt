package collection

fun main(array: Array<String>) {

    val mutableMap = mutableMapOf("111" to 23, Pair("222", 3333))

    mutableMap += "4444" to 222
    mutableMap += Pair("23", 232)
    mutableMap["2232"] = 10

    println(mutableMap)


    mutableMap.getOrPut("4442"){222}

    println(mutableMap)
}