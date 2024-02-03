package amitapps.media.swipeapp.repository

import amitapps.media.swipeapp.api.ProductAPI
import amitapps.media.swipeapp.models.Product
import amitapps.media.swipeapp.models.ProductItem
import amitapps.media.swipeapp.utils.NetworkResult
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject


class ProductRepository @Inject constructor(private val productAPI: ProductAPI) {

    private val _productResponseLiveData = MutableLiveData<NetworkResult<Product>>()
    val productResponseLiveData: LiveData<NetworkResult<Product>>
        get() = _productResponseLiveData

    suspend fun getProduct() {
        val response = productAPI.getProducts()
//        Log.d("ProductRepository", response.body().toString())
        if(response.isSuccessful && response.body() != null) {
            _productResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if(response.errorBody() != null) {
            _productResponseLiveData.postValue(NetworkResult.Error(response.message()))
        } else {
            _productResponseLiveData.postValue(NetworkResult.Error("Api not working repository"))
        }

    }
}