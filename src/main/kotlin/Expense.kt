import com.google.gson.annotations.SerializedName

data class Expense (
    @SerializedName("id") override  var id: Int,
    @SerializedName("name") override var name: String,
    @SerializedName("costs") var costs: Number,
    @SerializedName("category") var category: Category)
    :Catalog{



init{
    id = counter
}
    companion object {
        private var counter = 0

        fun updateCounter(id: Int) {
            counter = id + 1
        }
    }
    constructor(name: String, costs: Number, category: Category) : this(counter, name, costs, category)

}


