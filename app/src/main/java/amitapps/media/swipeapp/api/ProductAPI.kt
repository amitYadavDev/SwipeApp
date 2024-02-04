package amitapps.media.swipeapp.api

import amitapps.media.swipeapp.models.AddProductItem
import amitapps.media.swipeapp.models.AddProductResponse
import amitapps.media.swipeapp.models.Product
import amitapps.media.swipeapp.models.ProductItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.RequestBody

interface ProductAPI {
    @GET("/api/public/get") // Replace with your actual API endpoint
    suspend fun getProducts(): Response<Product>

    @Multipart
    @POST("/api/public/add")
    suspend fun addProduct( @Part("product_name") productName: RequestBody,
                            @Part("product_type") productType: RequestBody,
                            @Part("price") price: RequestBody,
                            @Part("tax") tax: RequestBody): Response<AddProductResponse>
}