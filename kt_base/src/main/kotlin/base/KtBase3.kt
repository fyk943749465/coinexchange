package base

fun main(args: Array<String>) {
    try {
        var info: String? = null
        checkException(info)

    } catch (e: Exception) {
        println("Oops! $e")
    }

    val str : String = "Dany is a boy"
    val substring = str.substring(0 until str.indexOf("is"))
    println(substring)

    var str2: String ="Leggy,Harry,Ldeg,Marry,Petty,Scott,Tide"
    val strings = str2.split(",")

    val (var1, var2, var3, var4, var5) = strings
    println("v1:$var1, v2:$var2, v3:$var3, v4:$var4, v5:$var5")


    val sourcePwd = "ABCDEFGHIJKLMNOPQRST"
    println(sourcePwd)

    val r1 = sourcePwd.replace(Regex("AGTK")) {
        println(it.value)
        it.value
    }

    println(r1)

    val str3 = "Hello World"
    var newStr3 = str3.replace("o", "t")
    println(newStr3)

    val str4 = "Hello World"
    var newStr4 = str4.replace(Regex("[oW]")) {
        when(it.value){
            "o" -> "g"
            "W" -> "1"
            else->
                it.value
        }
    }
    println(newStr4)


}


fun checkException(info: String?) {
    info ?: throw CustomException()
}

class CustomException : IllegalArgumentException("too bad")



