package com.example.platzi.views.fragments.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.platzi.databinding.FragmentDetailBinding
import com.example.platzi.utilities.gone
import com.example.platzi.utilities.visible
import com.example.platzi.viewmodels.DetailViewModel
import com.example.platzi.views.adapters.DetailImageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailViewModel>()
    private val args: DetailFragmentArgs by navArgs()

    private val detailImageAdapter = DetailImageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = args.id.toInt()

        viewModel.getProductById(productId)
        observeData()


        val dotsIndicator = binding.indicator
        val viewPager = binding.viewPager2
        viewPager.adapter = detailImageAdapter
        dotsIndicator.attachTo(viewPager)
    }

    private fun observeData() {
        viewModel.item.observe(viewLifecycleOwner) { product ->
            product?.let {

                binding.textView.text = it.title
                binding.textView2.text = it.description.toString()
                detailImageAdapter.updateList(it.images ?: emptyList())

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
            }
        }
    }

    private fun hideOtherWidgets() {
        binding.detailConstraintLayout.gone()
    }

    private fun showOtherWidgets() {
        binding.detailConstraintLayout.visible()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
