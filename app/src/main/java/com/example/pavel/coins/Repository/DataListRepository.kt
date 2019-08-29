package com.example.pavel.coins.Repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.example.pavel.coins.Api.IMyApi
import com.example.pavel.coins.Model.Number
import com.example.pavel.coins.db.NumberDAO
import io.reactivex.disposables.CompositeDisposable
import kotlin.concurrent.thread

class DataListRepository(val api: IMyApi, val numberDAO: NumberDAO) {
    lateinit var pagedList: LiveData<PagedList<Number>>
    lateinit var dataSourceFactoryDataSource: FactoryDataSource

    fun fetchPagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Number>>{
        dataSourceFactoryDataSource = FactoryDataSource(api,compositeDisposable,numberDAO)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .build()

        pagedList = LivePagedListBuilder(dataSourceFactoryDataSource,config).build()

        return pagedList
    }


    fun getDbResults(compositeDisposable: CompositeDisposable): LiveData<PagedList<Number>>{
        dataSourceFactoryDataSource = FactoryDataSource(api,compositeDisposable,numberDAO)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .build()

        pagedList = LivePagedListBuilder(numberDAO.getAll(),config).build()

        return pagedList
    }

    fun getSortedName(compositeDisposable: CompositeDisposable): LiveData<PagedList<Number>>{
        dataSourceFactoryDataSource = FactoryDataSource(api,compositeDisposable,numberDAO)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .build()

        pagedList = LivePagedListBuilder(numberDAO.sortByName(),config).build()

        return pagedList
    }


    fun getSortedPrice(compositeDisposable: CompositeDisposable): LiveData<PagedList<Number>>{
        dataSourceFactoryDataSource = FactoryDataSource(api,compositeDisposable,numberDAO)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .build()

        pagedList = LivePagedListBuilder(numberDAO.sortByPrice(),config).build()

        return pagedList
    }

    fun getSearchText(str: String,compositeDisposable: CompositeDisposable): LiveData<PagedList<Number>>{
        dataSourceFactoryDataSource = FactoryDataSource(api,compositeDisposable,numberDAO)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .build()

        pagedList = LivePagedListBuilder(numberDAO.search(str),config).build()

        return pagedList
    }


    fun getDeleteFromFavourite(rank: Int,compositeDisposable: CompositeDisposable): LiveData<PagedList<Number>>{
        dataSourceFactoryDataSource = FactoryDataSource(api,compositeDisposable,numberDAO)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .build()
        thread {
            numberDAO.deleteFavourite(rank)
        }
       /* Observable.just(numberDAO.deleteFavourite(rank))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())*/
            //.subscribe({ Log.i("myLog","Success")},{Log.i("myLog","Success"+it.message)})
        thread {
            numberDAO.deleteFavourite(rank)
        }

        pagedList = LivePagedListBuilder(numberDAO.getAny(),config).build()

        return pagedList
    }

    fun addToFavourite(rank: Int,compositeDisposable: CompositeDisposable): LiveData<PagedList<Number>>{
        dataSourceFactoryDataSource = FactoryDataSource(api,compositeDisposable,numberDAO)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .build()

        thread {
            numberDAO.addToFavourite(rank)
        }
        /*Observable.just(numberDAO.addToFavourite(rank))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())*/
          //  .subscribe({ Log.i("myLog","Success update")},{Log.i("myLog","Success"+it.message)})
        pagedList = LivePagedListBuilder(numberDAO.getAny(),config).build()

        return pagedList
    }







    fun getNetworkState(): LiveData<NetWorkState> {
        return Transformations.switchMap<NetworkDataSource, NetWorkState>(
            dataSourceFactoryDataSource.dataLiveDataSource, NetworkDataSource::netWorkState)
    }
}