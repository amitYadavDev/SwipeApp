package amitapps.media.swipeapp.repository

import amitapps.media.swipeapp.api.ProductAPI
import amitapps.media.swipeapp.models.Product
import amitapps.media.swipeapp.models.ProductItem
import android.util.Log
import javax.inject.Inject


class ProductRepository @Inject constructor(private val productAPI: ProductAPI) {

    suspend fun getProduct(): Product? {
        val response = productAPI.getProducts()
//        Log.d("ProductRepository", response.body().toString())
        return response.body()
    }
}