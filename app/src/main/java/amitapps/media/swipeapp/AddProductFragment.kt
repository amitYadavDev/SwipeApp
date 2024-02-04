package amitapps.media.swipeapp

import amitapps.media.swipeapp.models.AddProductItem
import amitapps.media.swipeapp.models.AddWithImage
import amitapps.media.swipeapp.mvvm.ListingProductFragmentViewModel
import amitapps.media.swipeapp.utils.NetworkResult
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@AndroidEntryPoint
class AddProductFragment : BottomSheetDialogFragment() {
    private lateinit var productName: TextInputEditText
    private lateinit var productType: TextInputEditText
    private lateinit var productPrice: TextInputEditText
    private lateinit var productTax: TextInputEditText
    private lateinit var submit: Button
    private lateinit var ivProductImage: ImageView
    private lateinit var btnSelectImage: Button
    private var selectedImageUris: List<Uri> = emptyList()
    private val REQUEST_IMAGE_PICK = 100

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
        btnSelectImage = requireView().findViewById(R.id.btnSelectImage)
        ivProductImage = requireView().findViewById(R.id.ivProductImage)

        btnSelectImage.setOnClickListener {
            selectImage()
        }


        if(fieldShouldNotEmpty()) {
            submit.setOnClickListener {
                if(selectedImageUris.isNotEmpty()) {
                    val imageFiles = selectedImageUris.map { uri ->
                        File(getRealPathFromURI(uri))
                    }
                    val imageParts = imageFiles.map { file ->
                        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                        MultipartBody.Part.createFormData("files[]", file.name, requestFile)
                    }
                    val add = AddWithImage(productName.text.toString(), productType.text.toString(), productPrice.text.toString(),
                        productTax.text.toString(), imageParts)
                    productFragmentViewModel.addProductItemWithImage(add)
                } else {
                    val add = AddProductItem(productName.text.toString(), productType.text.toString(), productPrice.text.toString(),
                        productTax.text.toString())
                    productFragmentViewModel.addProduct(add)
                }

            }
        }
    }
    private fun fieldShouldNotEmpty(): Boolean {
        return productTax.toString().isNotBlank() && productName.toString().isNotBlank() &&
                productPrice.toString().isNotBlank() && productType.toString().isNotBlank()
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Handle the selected image URI
                val bitmap = getBitmapFromUri(uri)
                ivProductImage.setImageBitmap(bitmap)
                selectedImageUris = listOf(uri)
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
    }
    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = activity?.contentResolver?.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val filePath = cursor?.getString(columnIndex ?: 0) ?: ""
        cursor?.close()
        return filePath
    }
}