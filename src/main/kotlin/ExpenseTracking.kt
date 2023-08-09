class ExpenseTracking {

  private  val expenseList = mutableListOf<Expense>()

  private  val categoriesList = mutableListOf<Category>()


    fun getCategoryList(): List<Category> {
        return categoriesList
    }
    @JvmName("getVitrataList1")
    fun getExpenseList(): List<Expense> {
        return expenseList
    }


    fun addExpense(expense: Expense) {
        expenseList.add(expense)
    }

    fun addCategory(category: Category) {
        categoriesList.add(category)
    }

    fun deleteExpense(id:Int){
      val del =  expenseList.find { it.id ==id }
        expenseList.remove(del)
    }

    fun getExpenseById(id: Int): Expense? {
        return expenseList.find { it.id == id }
    }

    fun getCategoryById(id: Int): Category? {
        return categoriesList.find { it.id == id }
    }

    fun updateExpenseById(id: Int, updatedName: String, updatedAmount: Number){
        val ExpenseToUpdate = expenseList.find { it.id == id }
        if (ExpenseToUpdate != null) {
            ExpenseToUpdate.name = updatedName
            ExpenseToUpdate.costs = updatedAmount
            println("Витрату з id $id було оновлено.")
        }
    }
}