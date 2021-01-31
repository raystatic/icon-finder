package com.raystatic.iconfinder.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.raystatic.iconfinder.data.IconsRepository
import com.raystatic.iconfinder.data.models.Icon

class SearchViewModel @ViewModelInject constructor(
    private val repository: IconsRepository
):ViewModel() {

    private val _selectedIcon = MutableLiveData<Icon?>()

    val selectedIcon:LiveData<Icon?> get() = _selectedIcon

    fun setSelectedIcon(icon: Icon){
        _selectedIcon.postValue(icon)
    }

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