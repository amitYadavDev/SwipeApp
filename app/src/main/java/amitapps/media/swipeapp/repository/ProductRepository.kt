package amitapps.media.swipeapp.repository

import amitapps.media.swipeapp.api.ProductAPI
import amitapps.media.swipeapp.models.AddProductItem
import amitapps.media.swipeapp.models.AddProductResponse
import amitapps.media.swipeapp.models.Product
import amitapps.media.swipeapp.models.ProductItem
import amitapps.media.swipeapp.utils.NetworkResult
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class ProductRepository @Inject constructor(private val productAPI: ProductAPI) {

    private val _productResponseLiveData = MutableLiveData<NetworkResult<Product>>()
    val productResponseLiveData: LiveData<NetworkResult<Product>>
        get() = _productResponseLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData get() = _statusLiveData

    suspend fun getProduct() {
        _productResponseLiveData.postValue(NetworkResult.Loading())
        val response = productAPI.getProducts()
//        Log.d("ProductRepository", response.body().toString())
        handleResponse(response)
    }

    suspend fun addProduct(addProductItem: AddProductItem) {
        val productName: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), addProductItem.product_name)
        val productType: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), addProductItem.product_type)
        val price: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            java.lang.String.valueOf(addProductItem.price)
        )
        val tax: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), java.lang.String.valueOf(addProductItem.tax))


        _statusLiveData.postValue(NetworkResult.Loading())
        val productAddedResponse = productAPI.addProduct(productName, productType, price, tax)

        if(productAddedResponse.isSuccessful && productAddedResponse.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(productAddedResponse.message()))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("product not added, something wrong"))
        }

        Log.d("productAddedResponse  ------>", productAddedResponse.message())
//        Toast.makeText(requ, productAddedResponse.message(), Toast.LENGTH_LONG).show()
    }

    private fun handleResponse(response: Response<Product>) {
        if (response.isSuccessful && response.body() != null) {
            _productResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val error = JSONObject(response.errorBody()!!.charStream().readText())
            _productResponseLiveData.postValue(NetworkResult.Error(error.getString("message")))
        } else {
            _productResponseLiveData.postValue(NetworkResult.Error("Api not working repository"))
        }
    }
}