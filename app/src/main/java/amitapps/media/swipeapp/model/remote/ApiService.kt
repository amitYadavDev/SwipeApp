package amitapps.media.swipeapp.model.remote

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/api/public/get") // Replace with your actual API endpoint
    suspend fun getProducts(): Response<Product>
}