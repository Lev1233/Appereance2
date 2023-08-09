data class Expense (var id:Int, var name:String, var costs: Number, var category: Category){

    companion object{
        var counter = 0
        fun createExpense(name: String, costs: Number, category: Category): Expense {
            val newExpense = Expense(counter, name, costs, category)
            counter++
            return newExpense
        }
    }
}


