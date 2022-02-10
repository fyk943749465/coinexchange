package collection

fun main(args: Array<String>) {

    var nums = 1 .. 100

    var num2 = 1 until  100
    var result = 0
    nums.forEach {
        result += it
    }
    println(result)

    for(a in nums step 2) {

    }

    val reversed = nums.reversed()

}

class Rect(var height: Int, var width: Int)