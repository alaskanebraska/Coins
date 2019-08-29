package com.example.pavel.coins.SingleCoin

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.example.pavel.coins.Model.Number
import com.example.pavel.coins.Model.Response
import com.example.pavel.coins.db.NumberDAO
import com.example.pavel.coins.Repository.NetWorkState
import io.reactivex.disposables.CompositeDisposable

class SingleCoinViewModel(private val coinDetailRepository: CoinDetailRepository, rank: Int,dao: NumberDAO): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val coinDetails: LiveData<Response> by lazy {
        coinDetailRepository.fetchSingleCoinDetails(compositeDisposable,rank)
    }

    val coinDetailsFromDb: LiveData<Number> by lazy {
        dao.getWithRank(rank)
    }

    val netWorkState: LiveData<NetWorkState> by lazy {
        coinDetailRepository.getCoinDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}