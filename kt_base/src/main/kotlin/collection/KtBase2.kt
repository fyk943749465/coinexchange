package collection

fun main() {
    val list = listOf("1", "2", "3", "4")
    val list2 = mutableListOf("1", "2", "3")

    list2.add(1,"5")
    println(list2)

    val list3 = mutableListOf("1", "A", "C")
    list3.removeFirst()
    list3.removeIf {
        it == "A"
    }

    println(list3)


    val list4 = mutableListOf("1", "b", "Dear")
    list4 += "c"
    println(list4)

    list4.forEach { println(it) }

    list4.forEachIndexed { index, s ->
        print("index $index ")
        println("element $s")
    }
}