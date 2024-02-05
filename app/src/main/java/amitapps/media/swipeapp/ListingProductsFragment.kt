package amitapps.media.swipeapp

import amitapps.media.swipeapp.api.ProductAPI
import amitapps.media.swipeapp.databinding.FragmentListingProductsBinding
import amitapps.media.swipeapp.models.AddProductItem
import amitapps.media.swipeapp.models.ProductItem
import amitapps.media.swipeapp.mvvm.ListingProductFragmentViewModel
import amitapps.media.swipeapp.utils.NetworkResult
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.SearchView
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

        productViewModel.getProduct()

        manager = GridLayoutManager(requireContext(), 2)


        binding.buttonAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_listingProductsFragment_to_addProductFragment)
            bindObserversForAddProduct()
        }

        bindObservers()
        // Set a listener on the SearchView
        searchProducts()
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

    private fun bindObserversForAddProduct() {
        productViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }

                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        it.message + "  AddProductFragment",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is NetworkResult.Loading -> {
//                    Toast.makeText(requireContext(), "data not able to post", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun bindObservers() {
        productViewModel.productResponseLiveData.observe(viewLifecycleOwner, Observer {

            //set binding.progressBar.isVisible = false
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
                    //set binding.progressBar.isVisible = true
                }
            }
        })
    }
}