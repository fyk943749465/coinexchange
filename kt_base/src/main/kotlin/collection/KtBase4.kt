package collection

fun main(args: Array<String>) {

    val ints = intArrayOf(1, 2, 3, 4)

    val elementAtOrElse = ints.elementAtOrElse(200){-1}
    println(elementAtOrElse)

    val arrays = listOf(1, 2, 3).toList()
    println(arrays)
}