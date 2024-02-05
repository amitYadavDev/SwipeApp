package amitapps.media.swipeapp

import amitapps.media.swipeapp.api.ProductAPI
import amitapps.media.swipeapp.databinding.FragmentListingProductsBinding
import amitapps.media.swipeapp.databinding.NoInternetConnectionBinding
import amitapps.media.swipeapp.models.AddProductItem
import amitapps.media.swipeapp.models.ProductItem
import amitapps.media.swipeapp.mvvm.ListingProductFragmentViewModel
import amitapps.media.swipeapp.utils.NetworkResult
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
    private lateinit var productAdapter: ProductAdapter
    private lateinit var manager: RecyclerView.LayoutManager

    private var _binding: FragmentListingProductsBinding? = null
    private val binding get() = _binding!!

    private var isConnected = true

    private var backPressedCount = 0
    private val BACK_PRESS_INTERVAL = 2000

    private val productViewModel by viewModels<ListingProductFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        productAdapter = ProductAdapter(emptyList())
        _binding = FragmentListingProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.searchView.clearFocus()

        manager = GridLayoutManager(requireContext(), 2)


        binding.buttonAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_listingProductsFragment_to_addProductFragment)
        }

        // Check for internet connectivity

        isConnected = isNetworkConnected(requireContext())
        // Show/hide the placeholder based on connectivity status
        binding.textViewNoInternet.visibility = View.VISIBLE
        binding.recyclerViewProducts.visibility = View.GONE

        if(isConnected) {
            binding.recyclerViewProducts.visibility = View.VISIBLE
            binding.textViewNoInternet.visibility = View.GONE
            productViewModel.getProduct()
            bindObservers()
        }

        searchProducts()
        

        // Set up back press handling
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackButtonPressed()
                }
            }
        )
    }
    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    private fun searchProducts() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productAdapter.filter(newText.orEmpty())
                Log.d("onQueryTextChange", newText.toString())
                return true
            }
        })
    }



    private fun bindObservers() {
        productViewModel.productResponseLiveData.observe(viewLifecycleOwner, Observer {

            //set binding.progressBar.isVisible = false
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    binding.recyclerViewProducts.apply {
                        val response = it.data as List<ProductItem>
                        productAdapter = ProductAdapter(response)
                        layoutManager = manager
                        adapter = productAdapter
                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }

                is NetworkResult.Loading -> {
                    //setup progress bar
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun onBackButtonPressed() {
        if (backPressedCount == 1) {
            // If back button pressed twice within the specified interval, exit the app
            requireActivity().finish()
        } else {
            // If it's the first press, show a message or perform any other action
             Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show()

            // Increment the back press count
            backPressedCount++
            binding.searchView.clearFocus()
            binding.searchView.setQuery("", false)

            // Reset the count after the specified interval
            view?.postDelayed({ backPressedCount = 0 }, BACK_PRESS_INTERVAL.toLong())
        }
    }
}