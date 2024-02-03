package amitapps.media.swipeapp.mvvm

import amitapps.media.swipeapp.models.AddProductItem
import amitapps.media.swipeapp.models.Product
import amitapps.media.swipeapp.models.ProductItem
import amitapps.media.swipeapp.repository.ProductRepository
import amitapps.media.swipeapp.utils.NetworkResult
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingProductFragmentViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val productResponseLiveData: LiveData<NetworkResult<Product>>
        get() = productRepository.productResponseLiveData
    val statusLiveData get() = productRepository.statusLiveData

    fun addProduct(addProductItem: AddProductItem) {
        viewModelScope.launch {
            productRepository.addProduct(addProductItem)
        }
    }

    fun getProduct() {
        viewModelScope.launch {
            productRepository.getProduct()
        }
    }
}