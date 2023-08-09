import java.io.File
import com.google.gson.Gson
import extention.printCategories
import extention.printExpenses


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
    val data = gson.fromJson(jsonContent, Pair::class.java)

    val categories: List<Category> = gson.fromJson(data.first.toString(), Array<Category>::class.java).toList()
    val expenses: List<Expense> = gson.fromJson(data.second.toString(), Array<Expense>::class.java).toList()

    for (category in categories) {
      //  println("Category ID: ${category.id}, Name: ${category.name}")
        oblik.addCategory(category)
    }
    Category.counter = categories.last().id

    for (expense in expenses) {
      //  println("Expense ID: ${expense.id}, Name: ${expense.name}, Costs: ${expense.costs}, Category: ${expense.category}")
        oblik.addExpense(expense)
    }
    Expense.counter = expenses.last().id
}


object AppAppearance {
    var appExit: Boolean = true



    fun bootsTrap(){
        val categoryNameList = mutableListOf("Їжа", "Транспорт", "Навчання")
        for (nameCategory in categoryNameList){
            val category = Category.createCategory(nameCategory)
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
            oblik.addExpense(Expense.createExpense(name, costs, category))
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
            "gistogramma"->showGistopgram()
            "showCommands"-> allComands()

            "exit" -> exitApp()
            else -> commandNotFound()
        }
        private fun commandNotFound() = "немає такої команди!"


    private fun showGistopgram()  {
        val expenseList = oblik.getExpenseList()

        if (expenseList.isEmpty()) {
            println("Немає витрат для створення гістограмми")
            return
        }
        println("Гістограмма витрат:")
        for (expense in expenseList) {
            val expenseAmount = expense.costs.toDouble()
            val expenseName = expense.name
            val barLength = (expenseAmount / 10).toInt()

            println("$expenseName - ${expenseAmount} грн: ${"▓".repeat(barLength)}")
        }
    }


    private fun displayVitrata() {

        oblik.getExpenseList().printExpenses()

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
        oblik.getCategoryList().printCategories()


        val selectedCategoryId = readLine()?.toIntOrNull() ?: -1
        val selectedCategory = oblik.getCategoryList().find { it.id == selectedCategoryId }

        if (selectedCategory==null){
            println("Спочатку додайте категорію :")
              addCategory()
        }

        selectedCategory?.let {
            if (inputCostVitrata != null) {
                 oblik.addExpense(Expense.createExpense(inputNameVitrata,vitrataCost, it))
            }
        }
    }



    private fun addCategory() {

        println("Додати категорію уведіть ім'я :")
        val inputNameCategory = readLine()?:""
        val category =  Category.createCategory(inputNameCategory)
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

        oblik.getCategoryList().printCategories()

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
        oblik.getExpenseList().printExpenses()

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

        oblik.getExpenseList().printExpenses()

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

