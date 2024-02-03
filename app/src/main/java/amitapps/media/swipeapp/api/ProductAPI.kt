package amitapps.media.swipeapp.api

import amitapps.media.swipeapp.models.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductAPI {
    @GET("/api/public/get") // Replace with your actual API endpoint
    suspend fun getProducts(): Response<Product>
}