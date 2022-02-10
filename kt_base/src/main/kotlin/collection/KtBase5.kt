package collection

fun main(args: Array<String>) {

    val mMap1 = mapOf("Hello" to 123, Pair("Hi", 234), "tt" to(2332))

    mMap1.forEach { t, u ->
        println(t)
        println(u)
    }

    for(item in mMap1) {
        println(item.key)
        println(item.value)
    }

    println(mMap1["Hello"]) //
    
    println(mMap1.getOrDefault("Hello", -1))
    println(mMap1.getOrElse("Hi"){-1})

    val value = mMap1.getValue("t")


}