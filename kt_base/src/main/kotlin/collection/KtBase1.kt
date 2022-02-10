package collection

fun main(args: Array<String>) {

    val list = listOf("Derry", "Zhangsan", "Lisi", "Wangwu")

    println(list.getOrElse(3){});
    println(list.getOrElse(10){})
    println(list.getOrNull(110))
}