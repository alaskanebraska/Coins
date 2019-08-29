package com.example.pavel.coins.MainActivity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.example.pavel.coins.Model.Data
import com.example.pavel.coins.Model.Number
import com.example.pavel.coins.Repository.DataListRepository
import com.example.pavel.coins.Repository.NetWorkState
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(val dataListRepository: DataListRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val pagedList: LiveData<PagedList<Number>> by lazy {
        dataListRepository.fetchPagedList(compositeDisposable)
    }

    val pagedListFromDb: LiveData<PagedList<Number>> by lazy {
        dataListRepository.getDbResults(compositeDisposable)
    }

    fun search(str: String): LiveData<PagedList<Number>>{
        return dataListRepository.getSearchText(str,compositeDisposable)
    }

    val pagedSortingByName: LiveData<PagedList<Number>> by lazy {
        dataListRepository.getSortedName(compositeDisposable)
    }

    val pagedSortingByPrice: LiveData<PagedList<Number>> by lazy {
        dataListRepository.getSortedPrice(compositeDisposable)
    }


    val netWorkState: LiveData<NetWorkState> by lazy {
        dataListRepository.getNetworkState()
    }

    fun deleteFromFavourite(rank: Int): LiveData<PagedList<Number>>{
        return dataListRepository.getDeleteFromFavourite(rank,compositeDisposable)
    }

    fun addToFavourite(rank: Int): LiveData<PagedList<Number>>{
        return dataListRepository.addToFavourite(rank,compositeDisposable)
    }


    fun listIsEmpty(): Boolean{
        return pagedList.value?.isEmpty() ?: true
    }

    fun refresh(){
        dataListRepository.dataSourceFactoryDataSource.dataLiveDataSource.value!!.invalidate()
    }


}