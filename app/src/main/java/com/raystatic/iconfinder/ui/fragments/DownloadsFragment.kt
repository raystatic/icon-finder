package com.raystatic.iconfinder.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.raystatic.iconfinder.R
import com.raystatic.iconfinder.databinding.FragmentDownloadsBinding

class DownloadsFragment: Fragment(R.layout.fragment_downloads) {

    private var _binding: FragmentDownloadsBinding?=null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDownloadsBinding.bind(view)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}