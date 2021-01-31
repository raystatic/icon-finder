package com.raystatic.iconfinder.ui.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raystatic.iconfinder.R
import com.raystatic.iconfinder.data.models.DownloadedIcon
import com.raystatic.iconfinder.databinding.BottomSheetDownloadBinding
import com.raystatic.iconfinder.ui.viewmodels.SearchViewModel
import com.raystatic.iconfinder.utils.Constants
import com.raystatic.iconfinder.utils.Status
import com.raystatic.iconfinder.utils.Utility
import com.raystatic.iconfinder.utils.ViewExtension.showCustomSnack
import com.raystatic.iconfinder.utils.ViewExtension.showSnack
import com.raystatic.iconfinder.utils.ViewExtension.showToast
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.io.File

private const val REQUEST_WRITE_PERMISSION = 0
@AndroidEntryPoint
class DownloadBottomSheetFragment: BottomSheetDialogFragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: BottomSheetDownloadBinding?=null
    private val binding get() = _binding!!

    private val viewmodel by activityViewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetDownloadBinding.inflate(inflater,container, false)

        binding.btnDownload.setOnClickListener {
            if (selectedDownloadUrl.isNotEmpty()) {
                viewmodel.downloadIcon(selectedDownloadUrl)
            } else {
                binding.radioGroup.showToast(requireContext(),Constants.SELECT_SIZE)
            }
        }

        subscribeToObservers()

        return binding.root
    }

    private fun subscribeToObservers() {
        viewmodel.selectedIcon.observe(viewLifecycleOwner, {
            it?.let { icon ->
                icon.raster_sizes.forEachIndexed { index, raster ->
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
                    selectedDownloadUrl = icon.raster_sizes[checkedId].formats[0].download_url
                    Glide.with(requireContext())
                        .load(raster.formats[0].preview_url)
                        .apply(RequestOptions().override(raster.size_width, raster.size_height))
                        .into(binding.imgPreview)
                }

            }
        })

        viewmodel.downloadResponse.observe(viewLifecycleOwner,{
            when(it.status){
                Status.SUCCESS ->{
                    it.data?.let { responseBody ->
                        viewmodel.writeFileToDevice(responseBody, requireContext())
                        disableDownloadButton()
                    } ?: kotlin.run {
                        binding.root.showToast(requireContext(),Constants.SOMETHING_WENT_WRONG)
                        enableDownloadButton()
                    }
                }
                Status.LOADING -> {
                    disableDownloadButton()
                    binding.btnDownload.text = Constants.DOWNLOADING
                }
                Status.ERROR -> {
                    enableDownloadButton()
                    binding.root.showToast(requireContext(),it.message.toString())
                    binding.btnDownload.text = Constants.DOWNLOAD_FILE
                }
            }
        })

        viewmodel.savedFile.observe(viewLifecycleOwner, {
            when(it.status){
                Status.SUCCESS ->{
                    it.data?.let { path ->
                        Timber.d("File downloaded at $path")
                        binding.root.showToast(requireContext(),Constants.DOWNLOAD_SUCCESS)
                        val file = File(path)
                        if (file.length() > 0){
                            viewmodel.insertDownloadedIcon(DownloadedIcon(path,Utility.findDate(path)))
                        }
                    } ?: kotlin.run {
                        binding.root.showToast(requireContext(),Constants.SOMETHING_WENT_WRONG)
                    }

                    enableDownloadButton()
                    binding.btnDownload.text = Constants.DOWNLOAD_FILE
                }
                Status.LOADING -> {
                    disableDownloadButton()
                    binding.btnDownload.text = Constants.DOWNLOADING
                }
                Status.ERROR -> {
                    enableDownloadButton()
                    binding.root.showToast(requireContext(),it.message.toString())
                    binding.btnDownload.text = Constants.DOWNLOAD_FILE
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()

        selectedDownloadUrl = ""

    }

    private fun requestPermission() {
        if (Utility.hasWritePermission(requireContext())) return

        EasyPermissions.requestPermissions(
            this,
            getString(R.string.accept_write_permission),
            REQUEST_WRITE_PERMISSION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        enableDownloadButton()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        disableDownloadButton()
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            requestPermission()
        }
    }

    private fun enableDownloadButton(){
        binding.btnDownload.apply {
            isEnabled = true
        }
    }

    private fun disableDownloadButton(){
        binding.btnDownload.apply {
            isEnabled = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private var selectedDownloadUrl = ""
    }

}