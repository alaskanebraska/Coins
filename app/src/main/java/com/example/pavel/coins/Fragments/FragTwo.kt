package com.example.pavel.coins.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pavel.coins.Model.ResponseTwo
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.frag2.*
import android.util.Log
import com.example.pavel.coins.Api.IMyApi
import com.example.pavel.coins.Api.RetrofitClient
import com.example.pavel.coins.R
import io.reactivex.disposables.Disposable


class FragTwo: Fragment() {
    lateinit var api: IMyApi
    val TAG: String = "myLog"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.frag2, container, false)
        var retrofit = RetrofitClient.instance
        api = retrofit.create(IMyApi::class.java)
        var observable: Observable<ResponseTwo>
        observable = api.getDetails()
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<ResponseTwo> {
                override fun onComplete() {

                }
                override fun onSubscribe(d: Disposable) {

                }
                override fun onNext(t: ResponseTwo) {
                    active_cryptocurrencies.text = t.data.active_cryptocurrencies.toString()
                    active_market_pairs.text = t.data.active_market_pairs.toString()
                    active_exchanges.text = t.data.active_exchanges.toString()
                    eth_dominance.text = t.data.eth_dominance.toString()
                    btc_dominance.text = t.data.btc_dominance.toString()
                    altcoin_volume_24h.text = t.data.quote.USD.percent_change_1h.toString()
                    altcoin_market_cap.text = t.data.last_updated
                }

                fun onCompleted() {
                    Log.i(TAG,"finished")
                }

                override fun onError(e: Throwable) {
                    Log.i(TAG,"your error"+ e.toString())
                }

            })



        return rootView;
    }


}