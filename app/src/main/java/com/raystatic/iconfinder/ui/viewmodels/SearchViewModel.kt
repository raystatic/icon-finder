package com.raystatic.iconfinder.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.raystatic.iconfinder.data.IconsRepository

class SearchViewModel @ViewModelInject constructor(
    private val repository: IconsRepository
):ViewModel() {

    private val currentIconQuery = MutableLiveData(DEFAULT_QUERY)

    val icons = currentIconQuery.switchMap { currentIconQuery->
        repository.getSearchResult(query = currentIconQuery).cachedIn(viewModelScope)
    }

    fun searchIcons(query:String){
        currentIconQuery.value = query
    }

    companion object{
        private const val DEFAULT_QUERY = "arrow"
    }

}