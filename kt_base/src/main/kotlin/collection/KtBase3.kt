package collection

fun main(args: Array<String>) {

    val list = listOf("1", "2", "3")

    val(_, v1, v2) = list

    val set = mutableSetOf("1", "2", "3")
    set += "6"

    println(set)

    set.elementAtOrElse(1){}

    set.distinct()

}