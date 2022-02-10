fun main(args: Array<String>) {

    val str = "ABC"
    str.forEach { println(it) }

    val num = "43".toIntOrNull()
    println(num)

    val num2 = 23.56.toInt()
    println(num2)

    val format = "%.10f".format(23.46)
    println(format)

    str.apply {

    }.apply {

    }.apply {

    }

    val list = listOf(1, 2, 3, 4 ,5)
    val result = list.let {
        println(it)
        it.first() + it.first()
    }
    println(result)
}