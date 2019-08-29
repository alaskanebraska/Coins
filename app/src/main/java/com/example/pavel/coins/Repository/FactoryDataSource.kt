package com.example.pavel.coins.Repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.example.pavel.coins.Api.IMyApi
import com.example.pavel.coins.Model.Number
import com.example.pavel.coins.db.NumberDAO
import io.reactivex.disposables.CompositeDisposable

class FactoryDataSource(val api: IMyApi, val compositeDisposable: CompositeDisposable, val numberDAO: NumberDAO):DataSource.Factory<Int,Number>() {

    val dataLiveDataSource = MutableLiveData<NetworkDataSource>()
    override fun create(): DataSource<Int, Number> {
        val dataSource = NetworkDataSource(api,compositeDisposable,numberDAO)
        dataLiveDataSource.postValue(dataSource)
        return dataSource
    }


}