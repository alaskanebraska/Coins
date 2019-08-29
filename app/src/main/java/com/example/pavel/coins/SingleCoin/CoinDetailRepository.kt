package com.example.pavel.coins.SingleCoin

import android.arch.lifecycle.LiveData
import com.example.pavel.coins.Api.IMyApi
import com.example.pavel.coins.Model.Response
import com.example.pavel.coins.Repository.DetailsNetworkDataSource
import com.example.pavel.coins.Repository.NetWorkState
import io.reactivex.disposables.CompositeDisposable

class CoinDetailRepository(private val api: IMyApi) {
    lateinit var detailsNetworkDataSource: DetailsNetworkDataSource

    fun fetchSingleCoinDetails(compositeDisposable: CompositeDisposable, rank: Int) : LiveData<Response>{
        detailsNetworkDataSource = DetailsNetworkDataSource(api,compositeDisposable)
        detailsNetworkDataSource.fetchCoinDetails(rank)

        return detailsNetworkDataSource.downloadedCoinResponse
    }


    fun getCoinDetailsNetworkState(): LiveData<NetWorkState>{
        return detailsNetworkDataSource.networkState
    }
}