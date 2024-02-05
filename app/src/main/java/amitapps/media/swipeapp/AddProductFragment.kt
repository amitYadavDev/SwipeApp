package amitapps.media.swipeapp

import amitapps.media.swipeapp.databinding.FragmentAddProduct2Binding
import amitapps.media.swipeapp.models.AddProductItem
import amitapps.media.swipeapp.models.AddWithImage
import amitapps.media.swipeapp.mvvm.ListingProductFragmentViewModel
import amitapps.media.swipeapp.utils.Constants
import amitapps.media.swipeapp.utils.NetworkResult
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private var productType: String = Constants.ProductType.Product.toString()

    private var selectedImageUris: List<Uri> = emptyList()
    private val REQUEST_IMAGE_PICK = 100

    private var _binding: FragmentAddProduct2Binding? = null
    private val binding get() = _binding!!

    private val productFragmentViewModel by viewModels<ListingProductFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddProduct2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addData()
    }

    private fun addData() {
        selectProductType()


        binding.btnSelectImage.setOnClickListener {
            selectImage()
        }

        handleAddProduct()

        bindObservers()
    }

    private fun handleAddProduct() {
        binding.btnSubmit.setOnClickListener {
            if (fieldShouldNotEmpty()) {
                if (selectedImageUris.isNotEmpty()) {
                    val imageFiles = selectedImageUris.map { uri ->
                        File(getRealPathFromURI(uri))
                    }
                    val imageParts = imageFiles.map { file ->
                        val requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file)
                        MultipartBody.Part.createFormData("files[]", file.name, requestFile)
                    }
                    val add = AddWithImage(
                        binding.etProductName.text.toString(),
                        productType,
                        binding.etPrice.text.toString(),
                        binding.etTax.text.toString(),
                        imageParts
                    )
                    productFragmentViewModel.addProductItemWithImage(add)
                } else {
                    val add = AddProductItem(
                        binding.etProductName.text.toString(),
                        productType,
                        binding.etPrice.text.toString(),
                        binding.etTax.text.toString()
                    )
                    productFragmentViewModel.addProduct(add)
//                    dismiss()
                }

            }
        }
    }

    private fun selectProductType() {
        // Get the enum values as an array
        val productTypes = Constants.ProductType.values()

        val adapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, productTypes.map { it.name })

        // Apply the adapter to the AutoCompleteTextView
        binding.spinnerProductType.setAdapter(adapter)

        binding.spinnerProductType.setOnItemClickListener { parent, view, position, id ->
            val selectedProductType = parent.adapter.getItem(position)
            productType = selectedProductType.toString()
//            Log.d("autoCompleteTextView  ", productType)
        }
//        autoCompleteTextView.setOnDismissListener {
//            // Handle the case where nothing is selected (dropdown is dismissed)
//            productType = Constants.ProductType.Service.toString()
//            Log.d("autoCompleteTextView  ", " e service hai")
//        }
    }

    private fun fieldShouldNotEmpty(): Boolean {
        Log.d("fieldShouldNotEmpty", "  ${binding.etProductName.text.toString().isBlank()}")

        if (binding.etProductName.text.isNullOrBlank()) {
            binding.etProductName.requestFocus()
            return false
        } else if (binding.etPrice.text.isNullOrBlank()) {
            binding.etPrice.requestFocus()
            return false
        } else if (binding.etTax.text.isNullOrBlank()) {
            binding.etTax.requestFocus()
            return false
        }
        return true
    }

    private fun bindObservers() {
        productFragmentViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                    Toast.makeText(requireContext(), "Product added Successfully!", Toast.LENGTH_LONG).show()
                }

                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is NetworkResult.Loading -> {
//                    Toast.makeText(requireContext(), "data not able to post", Toast.LENGTH_LONG).show()
                }
            }
        })

    }


    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 101

    private fun selectImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
            Log.d("Permission_not_granted", " some message")
            // [TODO] need to fix it
            openImagePicker()
        } else {
            // Permission is already granted, proceed with image selection
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with image selection
                openImagePicker()
            } else {
                // Permission denied, show a message or handle it accordingly
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Handle the selected image URI
                val bitmap = getBitmapFromUri(uri)
                binding.ivProductImage.setImageBitmap(bitmap)
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