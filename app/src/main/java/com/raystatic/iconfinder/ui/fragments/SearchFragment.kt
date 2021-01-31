package com.raystatic.iconfinder.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.raystatic.iconfinder.R
import com.raystatic.iconfinder.databinding.FragmentSearchBinding
import com.raystatic.iconfinder.ui.adapters.IconAdapter
import com.raystatic.iconfinder.ui.adapters.IconLoadStateAdapter
import com.raystatic.iconfinder.ui.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search) {

    private val viewmodel by activityViewModels<SearchViewModel>()
    private var _binding: FragmentSearchBinding?=null
    private val binding get() = _binding!!
    private lateinit var iconAdapter: IconAdapter
    private lateinit var downloadBottomSheetFragment: DownloadBottomSheetFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        initUI()

        subscribeToObservers()

        setHasOptionsMenu(true)

    }

    private fun subscribeToObservers() {
        viewmodel.icons.observe(viewLifecycleOwner,{
            iconAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    private fun initUI() {

        downloadBottomSheetFragment = DownloadBottomSheetFragment()

        iconAdapter = IconAdapter(requireContext()){icon ->
            viewmodel.setSelectedIcon(icon)
            downloadBottomSheetFragment.show(childFragmentManager,downloadBottomSheetFragment.tag)
        }
        binding.apply {
            rvIcons.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
                itemAnimator = null
                adapter = iconAdapter.withLoadStateHeaderAndFooter(
                    header =IconLoadStateAdapter{iconAdapter.retry()},
                    footer = IconLoadStateAdapter{iconAdapter.retry()}
                )
            }
            btnRetry.setOnClickListener {
                iconAdapter.retry()
            }
        }

        iconAdapter.addLoadStateListener {loadState ->
            binding.apply {
                progress_bar.isVisible = loadState.source.refresh is LoadState.Loading
                rvIcons.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                tvError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    iconAdapter.itemCount < 1){
                    rvIcons.isVisible = false
                    tvEmpty.isVisible = true
                }else{
                    tvEmpty.isVisible = false
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.rvIcons.scrollToPosition(0)
                    viewmodel.searchIcons(it)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}