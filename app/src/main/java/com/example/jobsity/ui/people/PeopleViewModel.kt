package com.example.jobsity.ui.people

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.data.classes.PersonResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val repository: PeopleRepository
): ViewModel() {

    //private val repository = PeopleRepository()

    enum class SearchPersonStatus {ERROR, LOADING, DONE}

    private val _personData =  MutableLiveData<List<PersonResult>>()
    val personData: LiveData<List<PersonResult>> = _personData

    private val _personDataStatus = MutableLiveData<SearchPersonStatus>()

    fun searchPerson(name: String) {
        viewModelScope.launch {
            SearchPersonStatus.LOADING
            try {
                _personData.value = repository.searchPerson(name)
                _personDataStatus.value = SearchPersonStatus.DONE
            } catch (E: Exception){
                _personDataStatus.value = SearchPersonStatus.ERROR
                _personData.value = listOf()
                Log.d("ret_ee", E.toString())
            }
        }
    }
}