package amitapps.media.swipeapp.models

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class AddWithImage(
    @SerializedName("product_name")
    val product_name: String,
    @SerializedName("product_type")
    val product_type: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("tax")
    val tax: String,
    @SerializedName("files")
    val files: List<MultipartBody.Part>
)
