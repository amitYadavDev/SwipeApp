package amitapps.media.swipeapp

import amitapps.media.swipeapp.models.AddProductItem
import amitapps.media.swipeapp.mvvm.ListingProductFragmentViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddProductFragment : BottomSheetDialogFragment() {
    private lateinit var productName: TextInputEditText
    private lateinit var productType: TextInputEditText
    private lateinit var productPrice: TextInputEditText
    private lateinit var productTax: TextInputEditText
    private lateinit var submit: Button
    private val productFragmentViewModel by viewModels<ListingProductFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addData()
    }

    private fun addData() {
        productName = requireView().findViewById(R.id.etProductName)
        productType = requireView().findViewById(R.id.etProductType)
        productPrice = requireView().findViewById(R.id.etPrice)
        productTax = requireView().findViewById(R.id.etTax)
        submit = requireView().findViewById(R.id.btnSubmit)

        if(fieldShouldNotEmpty()) {
            submit.setOnClickListener {
                productFragmentViewModel.addProduct(AddProductItem(productName.toString(), productType.toString(), productPrice.toString(),
                    productTax.toString()))
            }
        }
    }
    private fun fieldShouldNotEmpty(): Boolean {
        return productTax.toString().isNotBlank() && productName.toString().isNotBlank() &&
                productPrice.toString().isNotBlank() && productType.toString().isNotBlank()
    }
}