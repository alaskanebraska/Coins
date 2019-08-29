package com.example.pavel.coins.Repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import com.example.pavel.coins.Api.IMyApi
import com.example.pavel.coins.Model.Number
import com.example.pavel.coins.db.NumberDAO
import com.example.pavel.coins.Api.RetrofitClient.LIMIT
import com.example.pavel.coins.Api.RetrofitClient.START
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class NetworkDataSource(val api: IMyApi, val compositeDisposable: CompositeDisposable, val numberDAO: NumberDAO): PageKeyedDataSource<Int, Number>() {
    val TAG: String = "myLog"
    private var start = START
    private var limit = LIMIT
    val netWorkState: MutableLiveData<NetWorkState> = MutableLiveData()
    private val dbRepository: DbRepository = DbRepository(numberDAO)

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Number>) {
        netWorkState.postValue(NetWorkState.LOADING)

        compositeDisposable.add(
            api.getFullCoins(START, LIMIT)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        dbRepository.deleteAll()
                        callback.onResult(it.data, null, start+30)
                        dbRepository.insert(it.data)
                        Log.i(TAG,"info"+ it.data.get(0).id)
                        Log.i(TAG,"info"+ it.data.get(0).name)
                        Log.i(TAG,"info"+ it.data.get(0).symbol)
                        Log.i(TAG,"info favor"+ it.data.get(0).favor)
                        Log.i(TAG,"info"+ it.data.get(0).quote!!.USD.percent_change_1h.toString())
                        Log.i(TAG,"info"+ it.data.get(0).quote!!.USD.price.toString())
                        Log.i(TAG,"info"+ it.data.get(3).symbol)
                        Log.i(TAG,"info"+ it.data.get(3).favor)
                        netWorkState.postValue(NetWorkState.LOADED)
                    },
                    {
                        netWorkState.postValue(NetWorkState.ERROR)
                        Log.i(TAG,"erroring"+ it.message)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Number>) {
        netWorkState.postValue(NetWorkState.LOADING)

        compositeDisposable.add(
            api.getFullCoins(params.key, LIMIT)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(LIMIT <= params.key){
                         //   dbRepository.deleteAll()
                        callback.onResult(it.data, params.key+30)
                            dbRepository.insert(it.data)
                            Log.i(TAG, "loaded"+it.data.size)
                        netWorkState.postValue(NetWorkState.LOADED)}
                        else
                            netWorkState.postValue(NetWorkState.ERROR)
                        Log.i(TAG, "End of list")
                    },
                    {
                        netWorkState.postValue(NetWorkState.ERROR)
                        Log.i(TAG, "errrorooo"+it.message)
                    }
                )
        )

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Number>) {

    }





}