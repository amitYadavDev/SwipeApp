package amitapps.media.swipeapp.api

import amitapps.media.swipeapp.models.AddProductItem
import amitapps.media.swipeapp.models.AddProductResponse
import amitapps.media.swipeapp.models.Product
import amitapps.media.swipeapp.models.ProductItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductAPI {
    @GET("/api/public/get") // Replace with your actual API endpoint
    suspend fun getProducts(): Response<Product>

    @POST("/api/public/add")
    suspend fun addProduct(@Body addProductItem: AddProductItem): Response<AddProductResponse>
}