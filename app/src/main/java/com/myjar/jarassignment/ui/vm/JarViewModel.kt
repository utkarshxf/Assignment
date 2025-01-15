package com.myjar.jarassignment.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class JarViewModel : ViewModel() {

    private val _listStringData = MutableStateFlow<List<ComputerItem>>(emptyList())
    private val _searchItem = MutableStateFlow<List<ComputerItem>>(emptyList())
    val searchItem: StateFlow<List<ComputerItem>>
        get() = _searchItem

    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())

    fun fetchData() {
        viewModelScope.launch {
            repository.fetchResults()
                .collect{
                _listStringData.value = it
                    _searchItem.value = it
            }
        }
    }
    fun search(search:String?){
        viewModelScope.launch {
            if (search.isNullOrBlank()){
                _searchItem.value = _listStringData.value
                return@launch
            }
            _searchItem.value = _listStringData.value.filter {
                it.name.contains(search,ignoreCase = true)
            }
        }
    }
}