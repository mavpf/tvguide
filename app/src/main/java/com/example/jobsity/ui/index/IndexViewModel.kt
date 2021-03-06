package com.example.jobsity.ui.index

import androidx.lifecycle.*
import com.example.jobsity.data.classes.Favorites
import com.example.jobsity.data.classes.ShowIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(
    private val repository: IndexRepository
) : ViewModel() {


    //Livedata for favorites ID
    val getIdFavorites: LiveData<List<Int>> = repository.getIdFavorites.asLiveData()

    fun updateFavorite(dataset: ShowIndex) {
        var check: Int

        //Transform data from API format to ROOM format
        //Two fields, genre and image, are lists, and ROOM doesn't handle lists.
        //Index View and recycler are working with API data format, so doesn't make sense
        // to create another view, only because of two fields.
        val favoritesData = transformToFavorites(dataset)

        viewModelScope.launch {
            //Check if a favorite is already saved
            //Not allowed to have more than one id in the table
            //So if return 0, means that the app must insert the value in the table
            //If return one, means that it should be removed
            check = repository.getCountFavorites(dataset.id)

            //If 0, insert
            if (check == 0) {
                viewModelScope.launch {
                    repository.insertFavorite(favoritesData)
                }
            } else {
                //Else, remove
                viewModelScope.launch {
                    repository.deleteFavorite(favoritesData)
                }
            }
        }
    }

    //Function to transform API data to ROOM
    fun transformToFavorites(showIndex: ShowIndex): Favorites {

        return Favorites(
            showIndex.id,
            showIndex.name,
            showIndex.genres.joinToString(", "),
            showIndex.image?.medium.toString()
        )
    }



    //Enum class for API status
    enum class ShowIndexStatus { LOADING, ERROR, DONE }

    //Always start on first page
    var indexPage = 0

    //Page getter
    fun indexPage(): Int {
        return indexPage
    }


    //Page setter up
    fun indexPageIncrease() {
        indexPage = indexPage.inc()
    }

    //Livedata from API
    private val _showIndexLiveData = MutableLiveData<List<ShowIndex>>()
    val showIndexLiveData: LiveData<List<ShowIndex>> = _showIndexLiveData

    //Status from API
    private val _showIndexStatus = MutableLiveData<ShowIndexStatus>()
    val showIndexStatus: LiveData<ShowIndexStatus> = _showIndexStatus

    //Teste
    private val _showIndexData: MutableList<ShowIndex> = mutableListOf()
    val showIndexData = _showIndexData


    //Load initial info
    init {
        getShowIndex(indexPage)
    }

    //Get shows by page from repository
    fun getShowIndex(
        page: Int
    ) {
        viewModelScope.launch {
            ShowIndexStatus.LOADING
            try {
                _showIndexData.addAll(repository.getIndex(page))
                _showIndexStatus.value = ShowIndexStatus.DONE
            } catch (e: Exception) {
                _showIndexStatus.value = ShowIndexStatus.ERROR
            }
        }
    }

    //Get shows by name from repository
    fun getShowNames(
        name: String
    ) {
        viewModelScope.launch {
            _showIndexData.clear()
            ShowIndexStatus.LOADING
            try {
                val searchList: MutableList<ShowIndex> = mutableListOf()
                repository.getShowNames(name).forEach {
                    searchList.add(it.show)
                }
                _showIndexData.addAll(searchList)
                _showIndexStatus.value = ShowIndexStatus.DONE
            } catch (e: Exception) {
                _showIndexStatus.value = ShowIndexStatus.ERROR
            }
        }
    }
}