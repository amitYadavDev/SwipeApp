package amitapps.media.swipeapp

import amitapps.media.swipeapp.api.ProductAPI
import amitapps.media.swipeapp.mvvm.ListingProductFragmentViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class ListingProductsFragment : Fragment() {
    private lateinit var productAdapter: RecyclerView.Adapter<*>
    private lateinit var  recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager

    private val productViewModel by viewModels<ListingProductFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listing_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewProducts)
//        productAdapter = ProductAdapter(emptyList()) // Initialize with an empty list

        manager = LinearLayoutManager(requireContext())
        productViewModel.getProduct()


        // Fetch data using Retrofit
        fetchData()
    }
    private fun fetchData() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://app.getswipe.in/") // Replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val productAPI = retrofit.create(ProductAPI::class.java)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = productAPI.getProducts()
                Log.d("productAdapter_abc", response.body().toString())
                if (response.isSuccessful) {
                    recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerViewProducts).apply{
                        productAdapter = ProductAdapter(response.body()!!)
                        layoutManager = manager
                        adapter = productAdapter
                    }
                } else {
                    Log.d("productAdapter_abc", " response is empty()")
                    // Handle empty response
                }
            } catch (e: Exception) {
                // Handle network or API errors
            }
        }
    }
}