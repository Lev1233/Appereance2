import java.io.File
import com.google.gson.Gson
import extention.printList
import extention.updateCounter


val oblik = ExpenseTracking()


fun main() {

   loadJson()
  //AppAppearance.bootsTrap()
    AppAppearance.allComands()
    AppAppearance.start()
}

fun loadJson() {
    val gson = Gson()

    val fileName = "data.json"

    val jsonContent = File(fileName).readText()

    try {
        val (categoryData, expenseData) = gson.fromJson(jsonContent, Pair::class.java)

        val categories: List<Category> = gson.fromJson(categoryData.toString(), Array<Category>::class.java).toList()
        val expenses: List<Expense> = gson.fromJson(expenseData.toString(), Array<Expense>::class.java).toList()

        for (category in categories) {
            oblik.addCategory(category)
        }
        categories.updateCounter()
       // Category.updateCounter(categories.map { it.id }.maxOrNull() ?: 0)

        for (expense in expenses) {
            oblik.addExpense(expense)
        }
        expenses.updateCounter()
       // Expense.updateCounter(expenses.map { it.id }.maxOrNull() ?: 0)


    } catch (e: Exception) {
        println("Помилка при зчитуванні данних з JSON: ${e.message}")
    }
}


object AppAppearance {
    var appExit: Boolean = true



    fun bootsTrap(){
        val categoryNameList = mutableListOf("Їжа", "Транспорт", "Навчання")
        for (nameCategory in categoryNameList){
            val category = Category(nameCategory)
            oblik.addCategory(category)
        }
        val expenseData = listOf(
            mapOf("name" to "pizza", "costs" to 50.0, "category" to oblik.getCategoryById(0) ),
            mapOf("name" to "taxi", "costs" to 15.0, "category" to oblik.getCategoryById(1)),
            mapOf("name" to "udemy", "costs" to 600, "category" to oblik.getCategoryById(2))
        )
            expenseData.map { data ->
            val name = data["name"] as String
            val costs = data["costs"] as Number
            val category = data["category"] as Category
            oblik.addExpense(Expense(name, costs, category))
        }
    }

    fun  start() {
        while (appExit) {
            print("> Введіть свою команду: ")
            processCommand()
        }
    }
     fun allComands() {
        println("Усі доступні команди :\n" +
                "Додати категорію --> addCategory\n"+
                "Додати витрату --> addvitrata\n"+
                "Видалити витрату --> deleteVitrata\n"+
                 "Показати витрату  --> displayVitrataByID\n"+
                "Змінити витрату --> changeVitrata\n"+
                "Показати всі витрати за категорією --> AllVitrataByCategory\n"+
                "Відобразити гістрограмму --> gistogramma\n"+
                "Відобразити усі команди --> allComands\n" +
                "Вихід --> exit")
    }
        fun processCommand() = when (readLine()) {
            "addCategory" -> addCategory()
            "addvitrata" -> addVitrata()
            "deleteVitrata"->deleteVitrata()
            "displayVitrataByID"->displayVitrata()
            "changeVitrata"-> changeVitrata()
            "allVitrataByCategory"->displayAllVitrataByCategory()
            "gistogramma"-> showVerticalHistogramWithScaledHeights()
            "showCommands"-> allComands()

            "exit" -> exitApp()
            else -> commandNotFound()
        }
        private fun commandNotFound() = "немає такої команди!"



    private fun showVerticalHistogramWithScaledHeights() {
        val categoryExpensesMap = mutableMapOf<Category, List<Expense>>()

        val expenseList = oblik.getExpenseList()


        for (expense in expenseList) {
            val category = expense.category
            if (categoryExpensesMap.containsKey(category)) {
                val expenses = categoryExpensesMap[category]!!.toMutableList()
                expenses.add(expense)
                categoryExpensesMap[category] = expenses
            } else {
                categoryExpensesMap[category] = listOf(expense)
            }
        }

        if (categoryExpensesMap.isEmpty()) {
            println("Немає данних для гістрограммі")
            return
        }

        println("Гістограмма :")

        // Знаходимо максимальну суму витрат у одній категорії
        val maxCategoryCosts = categoryExpensesMap.values.map { expenses ->
            expenses.sumByDouble { it.costs.toDouble() }
        }.maxOrNull() ?: 0.0

        // Побудова вертикальної гістограми для кожної категорії.
        for (i in 50 downTo 1) {
            val row = StringBuilder()
            for (expenses in categoryExpensesMap.values) {
                val categoryCosts = expenses.sumByDouble { it.costs.toDouble() }
                val scaledHeight = (categoryCosts / maxCategoryCosts * 50).toInt()
                row.append(if (scaledHeight >= i) "▓" else " ")
                row.append(" ")
            }
            println(row)
        }


        println("_".repeat(categoryExpensesMap.size * 2))


        val columnNumbersRow = StringBuilder()
        for (index in 1..categoryExpensesMap.size) {
            columnNumbersRow.append("$index ")
        }
        println(columnNumbersRow)

        // Обчислити загальну суму витрат."
        val totalExpenses = categoryExpensesMap.values.flatten().sumByDouble { it.costs.toDouble() }

         // "Вивести дані у відсотковому співвідношенні."
        for ((index, category) in categoryExpensesMap.keys.withIndex()) {
            val categoryTotalCosts = categoryExpensesMap[category]?.sumByDouble { it.costs.toDouble() } ?: 0.0
            val percentage = (categoryTotalCosts / totalExpenses) * 100
            println("$index - ${category.name} ($categoryTotalCosts - ${categoryExpensesMap[category]?.size} items) - %.2f%%".format(percentage))
        }

    }






//    private fun showGistopgram()  {
//        val expenseList = oblik.getExpenseList()
//        //зробити гістрограмму по категоріях
//        if (expenseList.isEmpty()) {
//            println("Немає витрат для створення гістограмми")
//            return
//        }
//        println("Гістограмма витрат:")
//        for (expense in expenseList) {
//            val expenseAmount = expense.costs.toDouble()
//            val expenseName = expense.name
//            val barLength = (expenseAmount / 10).toInt()
//
//            println("$expenseName - ${expenseAmount} грн: ${"▓".repeat(barLength)}")
//        }
//    }


    private fun displayVitrata() {

        oblik.getExpenseList().printList()

        println("Введіть id витрати яку хочети подивитися: ")
        val inputID = readLine()?.toIntOrNull()

        if (inputID != null) {
            val obl =  oblik.getExpenseById(inputID)
            if (obl != null) {
                println("ID: ${obl.id}, Назва: ${obl.name}, Сума: ${obl.costs}, Категорія: ${obl.category.name}")
            }else{
                println("немає такого ID")
            }
        }
    }


    private fun addVitrata() {

        println("Додати витрату уведіть ім'я :")
        val inputNameVitrata = readLine()?:""

        println("Додати витрату уведіть ціну :")
        val inputCostVitrata = readLine()


        val vitrataCost: Number = if (inputCostVitrata != null) {
            if (inputCostVitrata.contains(".")) {
                inputCostVitrata.toDoubleOrNull() ?: 0.0
            } else {
                inputCostVitrata.toIntOrNull() ?: 0
            }
        } else {
            0
        }

        println("Оберіть ID потрібноі категоріі :")
        oblik.getCategoryList().printList()


        val selectedCategoryId = readLine()?.toIntOrNull() ?: -1
        val selectedCategory = oblik.getCategoryList().find { it.id == selectedCategoryId }

        if (selectedCategory==null){
            println("Спочатку додайте категорію :")
              addCategory()
        }

        selectedCategory?.let {
            if (inputCostVitrata != null) {
                 oblik.addExpense(Expense(inputNameVitrata,vitrataCost, it))
            }
        }
    }



    private fun addCategory() {

        println("Додати категорію уведіть ім'я :")
        val inputNameCategory = readLine()?:""
        val category =  Category(inputNameCategory)
        category.let { oblik.addCategory(it) }

    }

    private fun exitApp(){
        appExit = false
        val gson = Gson()
        val fileName = "data.json"

        val categories = oblik.getCategoryList()
        val expenses = oblik.getExpenseList()

        val data = Pair(categories, expenses)
        val jsonData = gson.toJson(data)

        File(fileName).writeText(jsonData)

        println("JSON data saved to $fileName")
    }

    private fun displayAllVitrataByCategory() {

        oblik.getCategoryList().printList()
        println("Введіть категорію ім'я :")
        val inputCategory = readLine()
        val vitrataCategory = oblik.getExpenseList().filter { it.category.name == inputCategory }

        if (vitrataCategory.isNotEmpty()) {
            for (vitrata in vitrataCategory) {
                println("ID: ${vitrata.id}, Сума: ${vitrata.costs}, Ім'я: ${vitrata.name}")
            }
        }else{
            println("Немає такоі категорії")
        }
    }

    private fun changeVitrata() {
        oblik.getExpenseList().printList()

        println("Введіть id витрати, яку хочете змінити:")
        val inputID = readLine()?.toIntOrNull()


        if (inputID?.let { oblik.getExpenseById(it) } !=null){
          val newVitrata = oblik.getExpenseById(inputID)

            println("Введіть нову назву витрати:")
            val updatedName = readLine() ?: newVitrata?.name

            println("Введіть нову суму витрати:")
            val newVitrataSum= readLine()
            val newVitrataCost: Number = if (newVitrataSum != null) {
                if (newVitrataSum.contains(".")) {
                    newVitrataSum.toDoubleOrNull() ?: 0.0
                } else {
                    newVitrataSum.toIntOrNull() ?: 0
                }
            } else {
                0
            }
            if (newVitrataSum != null) {
                if (updatedName != null) {
                    oblik.updateExpenseById(inputID,updatedName,newVitrataCost)
                }
            }
        }else{
            println("немає такого ID")
        }
    }

    private fun deleteVitrata() {
        println("Введіть ID витрати яку потрібно видалити :")

        oblik.getExpenseList().printList()

           val inputIdDelete = readLine()?.toIntOrNull()
           if (inputIdDelete != null) {
               if (oblik.getExpenseById(inputIdDelete)!=null) {
                   oblik.deleteExpense(inputIdDelete)
               }else{
                   println("немає такого ID")
               }
        }else{
               println("Не корректний ввод")
        }
    }
}

