import com.google.gson.annotations.SerializedName

data class Category (
    @SerializedName("id") override var id: Int,
     @SerializedName("name") override var name: String)
    :Catalog {


    init{
        id = counter
    }
    companion object {
        private var counter = 0

        fun updateCounter(id: Int) {
            counter =  id + 1
        }
    }
    constructor(name: String) : this(counter, name)

}

