package amitapps.media.swipeapp.mvvm

import amitapps.media.swipeapp.models.Product
import amitapps.media.swipeapp.repository.ProductRepository
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


    fun getProduct() {
        viewModelScope.launch {
           val response = productRepository.getProduct()
            Log.d("ProductRepositoryVM", response.toString())
        }
    }
}