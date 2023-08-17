package extention

import Catalog
import Category
import Expense


fun<T:Catalog> List<T>.printList() {
    forEach {
        println("ID: ${it.id}, Назва: ${it.name}")
    }
}

fun List<Catalog>.updateCounter() {
    val maxId = this.map { it.id }.maxOrNull() ?: 0

    if (this[0] is Category) {
        Category.updateCounter(maxId)

    } else if (this[0] is Expense) {
        Expense.updateCounter(maxId)
    }

}

