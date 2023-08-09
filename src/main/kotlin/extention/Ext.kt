package extention

import Category
import Expense

fun List<Category>.printCategories() {
    forEach {
        println("ID: ${it.id}, Назва: ${it.name}")
    }
}

fun List<Expense>.printExpenses() {
    forEach {
        println("ID: ${it.id}, Назва: ${it.name}")
    }
}