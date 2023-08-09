data class Category (val id:Int,val name: String) {

    companion object {

       var counter = 0

        fun createCategory(name: String): Category {
            val newCategory = Category(counter, name)
            counter++
            return newCategory
        }
    }
}