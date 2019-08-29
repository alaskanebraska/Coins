package com.example.pavel.coins.Repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.pavel.coins.Api.IMyApi
import com.example.pavel.coins.Model.Response
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class DetailsNetworkDataSource (private val apiService: IMyApi, private  val compositeDisposable: CompositeDisposable) {
    val TAG: String = "myLog"
    private val _networkState = MutableLiveData<NetWorkState>()
    val networkState: LiveData<NetWorkState>
    get() = _networkState

    private val _downloadedCoinDetailResponse = MutableLiveData<Response>()
    val downloadedCoinResponse: LiveData<Response>
    get() = _downloadedCoinDetailResponse

    fun fetchCoinDetails(rank: Int){
        _networkState.postValue(NetWorkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getFullCoins(rank,1).subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedCoinDetailResponse.postValue(it)
                            _networkState.postValue(NetWorkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetWorkState.ERROR)
                            Log.i(TAG,"error"+it.message)
                        }

                    )
            )
        }
        catch (e: Exception){
            Log.i(TAG,e.message)
        }
    }
}