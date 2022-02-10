 fun main(args: Array<String>) {

     val method1 = {
         v1: Double, v2: Float, v3: Int ->
         "v1:$v1, v2:$v2, v3:$v3"
     }

     println(method1(253.3, 342.3f, 23))

     val method2 = {
         num: Int ->
         when(num) {
             1 -> "Monday"
             2 -> "Tuesday"
             else -> {
                 num
             }
         }
     }

     println(method2(20))

     loginAPI("zhansna", "123456", responseResult = {
         msg: String, code: Int ->
         println(msg)
         println(code)
     })

     loginAPI("zhansna", "123456"){ msg: String, code: Int ->
         println(msg)
         println(code)
     }

     loginAPI("xdfs", "1223", ::responseResult)
 }

 fun responseResult(msg: String, code: Int) {
     println("msg: $msg, code: $code")
 }

 inline fun loginAPI(username: String, userpwd: String, responseResult:(String, Int)-> Unit) {
     if (webServiceLoginAPI(username, userpwd)) {
         responseResult("login success", 200)
     } else {
         responseResult("login fail", 401)
     }
 }

 fun webServiceLoginAPI(username: String, userpwd: String): Boolean {
     return true
 }