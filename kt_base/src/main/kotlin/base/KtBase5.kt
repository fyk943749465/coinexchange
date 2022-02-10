fun main(args: Array<String>) {

    val str = "DSfy"

    str.run(::isLong)
        .run(::showText)
        .run(::mapText)
        .run(::println)

    str.let(::isLong)
        .let(::showText)
        .let(::mapText)
        .let(::println)



}

fun isLong(str: String) = str.length > 5

fun showText(isLong: Boolean) = if(isLong) "Text is long" else "Text is short"

fun mapText(text: String) = "[ $text ]"

