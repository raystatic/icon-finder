package com.raystatic.iconfinder.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raystatic.iconfinder.databinding.BottomSheetDownloadBinding
import com.raystatic.iconfinder.ui.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadBottomSheetFragment: BottomSheetDialogFragment() {

    private var _binding: BottomSheetDownloadBinding?=null
    private val binding get() = _binding!!

    private val viewmodel by activityViewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetDownloadBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.selectedIcon.observe(viewLifecycleOwner,{
            it?.let { icon->
                icon.raster_sizes.forEachIndexed { index, raster->
                    val radioButton = RadioButton(requireContext())
                    radioButton.layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                    val radioButtonText = "${raster.size_width} X ${raster.size_height}"
                    radioButton.text = radioButtonText
                    radioButton.id = index
                    binding.radioGroup.addView(radioButton)
                }

                binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
                    val raster = icon.raster_sizes[checkedId]
                    Glide.with(requireContext())
                        .load(raster.formats[0].preview_url)
                        .apply(RequestOptions().override(raster.size_width, raster.size_height))
                        .into(binding.imgPreview)
                }

            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}