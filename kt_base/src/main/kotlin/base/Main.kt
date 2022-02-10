fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val info = "Derry is Success"

    println(info)

    val age = 98
    println(age)

    val number = 148
    if (number in 10..59) {
        println("fail")
    } else if (number in 0..9) {
        println("too bad")
    } else if (number in 60..100) {
        println("very well")
    } else {
        println("God")
    }

    val day = 5
    val dayinfo = when(day) {
        1 -> "Today is Monday"
        2 -> "Today is Tuesday"
        3 -> "Today is Wednesday"
        else -> {
            println("pig pig day, i don't know")
        }
    }
    println(dayinfo)


    val park ="Yellowstone National Park"
    val time = 6

    println("Today, I go to ${park} and stay there ${time} hours")

    val isLogin = false
    println("server response: ${if(isLogin) "Congratulations, Login success" else "Opps, Login fail"}")

    method1("scott", 20)
    method2("Jetty")

    loinAction(age = 99, usernum = "123", userpwd = "deay", username = "Kitty", phonenumber = "13411211111")

    var len = "Harry Potter".count()
    println(len)

    var length = "Harry Potter".count() {
        it == 'r'
    }
    println(length)


    val methodAction: () -> String

    methodAction = {
        val input = 999999
        "$input Derry"
    }

    println(methodAction())

    val methodAction2: (Int, Int, Int) -> String = {
        number1, number2, number3 ->
        val inputValue = 9999
        "$inputValue params: $number1, $number2, $number3"
    }

    println(methodAction2(2, 3, 5))


    val methodAction3 : (String) -> String = {"$it Derry"}
    println(methodAction3("dddd"))

}


private fun method1(name: String, age: Int): Int {
    println(name)
    println(age)
    return age
}

private fun method2(name: String = "lisha", age: Int = 20): String {
    println(name)
    println(age)
    return name
}

private fun loinAction(username: String, userpwd: String, phonenumber: String, age: Int, usernum: String) {

}