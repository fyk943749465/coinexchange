fun main(args: Array<String>) {

    val name = "ABCDEFG"
    val ans = name.takeIf {
        false
    } ?: "you are null"

    println(ans)

    val ans2 = name.takeUnless {
        false
    } ?: "you are not null"

    println(ans2)
}