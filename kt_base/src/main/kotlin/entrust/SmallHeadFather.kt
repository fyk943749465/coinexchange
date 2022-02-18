package entrust

class SmallHeadFather : IWashBowl by BigHeadSon{

    override fun washBowl() {
        println("小头爸爸开始洗碗")
        BigHeadSon.washBowl()
        println("小头爸爸洗完了腕，其实没有洗，是让儿子洗的，赚到了10块钱，分儿子一块钱")
    }
}