fun main(array: Array<String>) {

    val str = "ABCDEFG"

    val ans = str.also {
            println(it)
            "CDEF"
        }
        .also {
            println(it.length)
        }

    println(ans)
}

