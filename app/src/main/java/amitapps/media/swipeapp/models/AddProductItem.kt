package amitapps.media.swipeapp.models

import com.google.gson.annotations.SerializedName

data class AddProductItem(
    @SerializedName("product_name")
    val product_name: String,
    @SerializedName("product_type")
    val product_type: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("tax")
    val tax: String
//    val image: String?
)
