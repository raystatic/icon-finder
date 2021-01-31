package com.raystatic.iconfinder.ui.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.raystatic.iconfinder.BuildConfig
import com.raystatic.iconfinder.R
import com.raystatic.iconfinder.databinding.FragmentDownloadsBinding
import com.raystatic.iconfinder.ui.adapters.DownloadsAdapter
import com.raystatic.iconfinder.ui.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DownloadsFragment: Fragment(R.layout.fragment_downloads) {

    private var _binding: FragmentDownloadsBinding?=null
    private val binding get() = _binding!!
    private lateinit var downloadsAdapter: DownloadsAdapter
    private val viewmodel by activityViewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDownloadsBinding.bind(view)

        downloadsAdapter = DownloadsAdapter {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${BuildConfig.APPLICATION_ID}.provider",
                it
            )
            val intent = Intent(Intent.ACTION_SEND)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .putExtra(Intent.EXTRA_STREAM, uri)
                .setType("images/*")

            val chooser = Intent.createChooser(intent, "Share File")

            val resInfoList: List<ResolveInfo> = requireContext().packageManager
                .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireContext().grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            startActivity(chooser)
        }

        binding.rvIcons.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(
                requireContext(),
                2,
                GridLayoutManager.VERTICAL,
                false
            )
            adapter = downloadsAdapter
        }

        viewmodel.fetchDownloads(requireContext())

        subscribeToObservers()

    }

    private fun subscribeToObservers() {
        viewmodel.downloadedIcons.observe(viewLifecycleOwner, {
            it?.let { downloads ->
                downloadsAdapter.submitData(downloads)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}