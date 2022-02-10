fun main(args: Array<String>) {

    val show = show("zhangsan")
    println(show("nihao", 20))


    var name2: String ?
    // name2 = "  "
    name2 = "ssdf"

    var ans = name2?.let{
        if (it.isBlank()) {
            "Default"
        } else {
            it
        }
    }

    println(ans)


    var name3: String ? = null

    //name3!!.let {  }

    println(name3 ?: "ssss")
}

fun show(info: String): (String, Int) -> String {

    return {
        name: String, age: Int ->
        "i'm anonymous "
    }
}