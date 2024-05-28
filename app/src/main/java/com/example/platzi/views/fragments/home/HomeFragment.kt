package com.example.platzi.views.fragments.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.platzi.databinding.CustomcreateproductdialogBinding
import com.example.platzi.databinding.FragmentHomeBinding
import com.example.platzi.models.get.ProductResponse
import com.example.platzi.models.post.NewProduct
import com.example.platzi.utilities.FileUtils
import com.example.platzi.utilities.gone
import com.example.platzi.utilities.visible
import com.example.platzi.viewmodels.HomeViewModel
import com.example.platzi.views.adapters.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var productAdapter = ProductAdapter()

    private val viewModel by viewModels<HomeViewModel>()

    private var filePath: String? = null

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                filePath = FileUtils.getPath(requireContext(), uri) // Get the file path
                Log.d("File Path", filePath ?: "No file path found")
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeData()
        viewModel.getAllProducts()
        viewModel.getAllCategories()

        binding.inputSearch.addTextChangedListener { text ->
            val searchText = text.toString().trim()
            viewModel.searchProducts(searchText)
        }

        binding.sortimageview.setOnClickListener {
            customAlertDialog(requireContext())
        }
    }

    private fun setUpRecyclerView() {


            val gridLayoutManager = GridLayoutManager(context, 2)
            binding.recycleViewHome.layoutManager = gridLayoutManager
            binding.recycleViewHome.adapter = productAdapter

            productAdapter.setOnItemClickListener { productId ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(productId.toString())
                findNavController().navigate(action)
            }

    }

    private fun observeData() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            products?.let {
                productAdapter.updateList(it.toCollection(ArrayList<ProductResponse>()))
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBarContainer.progressBar2.visible()
                hideOtherWidgets()
            } else {
                binding.progressBarContainer.progressBar2.gone()
                showOtherWidgets()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                Log.e("responseError", it)
            }
        }
    }

    private fun hideOtherWidgets() {


            binding.headerContainerLayout.gone()
            binding.recycleViewHome.gone()

    }

    private fun showOtherWidgets() {

            binding.headerContainerLayout.visible()
            binding.recycleViewHome.visible()

    }

    private fun openFilePicker() {

        lifecycleScope.launch(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                filePickerLauncher.launch(intent)
            } else {
                Toast.makeText(context, "No app to handle file selection", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun customAlertDialog(context: Context) {

        lifecycleScope.launch(Dispatchers.Main) {
            val dialogBinding =
                CustomcreateproductdialogBinding.inflate(LayoutInflater.from(context))
            val dialog = AlertDialog.Builder(context).apply {
                setView(dialogBinding.root)
            }.create()

            dialog.show()

            viewModel.categories.observe(viewLifecycleOwner) { categories ->
                categories?.let {
                    val categoryNames = categories.map { it.name }
                    val adapter =
                        ArrayAdapter(context, android.R.layout.simple_spinner_item, categoryNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dialogBinding.planetsSpinner.adapter = adapter
                }
            }

            dialogBinding.button1.setOnClickListener {
                val editTextValue = dialogBinding.editTextText.text.toString()
                val editTextValue2 = dialogBinding.editTextText2.text.toString()
                val editTextValue3 = dialogBinding.editTextText3.text.toString()

                val selectedItemName = dialogBinding.planetsSpinner.selectedItem.toString()

                val selectedCategoryId =
                    viewModel.categories.value?.find { it.name == selectedItemName }?.id

                Log.d("Selected item ID", selectedCategoryId.toString())

                if (editTextValue.isNotBlank() && editTextValue.isNotEmpty() && filePath != null) {
                    dialog.dismiss()

                    val file = File(filePath!!)
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart =
                        MultipartBody.Part.createFormData("images", file.name, requestFile)

                    val newProduct = NewProduct(
                        editTextValue,
                        editTextValue2.toInt(),
                        editTextValue3,
                        selectedCategoryId ?: -1,
                        listOf(imagePart.toString())
                    )
                    viewModel.createProduct(newProduct, listOf(imagePart))
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please select a file and fill in all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            dialogBinding.buttonSelectFiles.setOnClickListener {
                openFilePicker()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
