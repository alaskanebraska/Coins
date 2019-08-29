package com.example.pavel.coins.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pavel.coins.SingleCoin.CoinDetailRepository
import com.example.pavel.coins.SingleCoin.SingleCoinViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.example.pavel.coins.MainActivity.AccessToInternet
import com.example.pavel.coins.Api.IMyApi
import com.example.pavel.coins.Api.RetrofitClient
import com.example.pavel.coins.Model.Number
import com.example.pavel.coins.Model.Response
import com.example.pavel.coins.R
import com.example.pavel.coins.Repository.NetWorkState
import com.example.pavel.coins.db.NumberDAO
import com.example.pavel.coins.db.NumberRoomDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.frag1.*

class FragOne: Fragment() {
    private lateinit var viewModel: SingleCoinViewModel
    private lateinit var coinDetailRepository: CoinDetailRepository
    val TAG: String = "myLog"
    private var rank: Int = 0
    private var position: Int = 0
    private lateinit var numberDAO: NumberDAO
    private var numberRoomDb: NumberRoomDatabase?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.frag1, container, false)
        rank = activity!!.intent.getIntExtra("rank",1)
        Log.i(TAG,"Rank get"+ rank.toString())
        // position = rank-1
        Log.i(TAG,"Position"+ position)
        numberRoomDb = NumberRoomDatabase.getInstance(activity!!)
        numberDAO = numberRoomDb!!.numberDao()
        val retrofit = RetrofitClient.instance
        val api: IMyApi = retrofit.create(IMyApi::class.java)
        coinDetailRepository = CoinDetailRepository(api)

        viewModel = getViewModel(rank)

        if(AccessToInternet.isOnline(activity!!)) {
            viewModel.coinDetails.observe(this, Observer {
                bindUI(it)
            })

            viewModel.netWorkState.observe(this, Observer {
                // progress_bar.visibility = if (it == NetWorkState.LOADING) View.VISIBLE else View.GONE
                txt_error.visibility = if (it == NetWorkState.ERROR) View.VISIBLE else View.GONE

            })
        }
        else
            viewModel.coinDetailsFromDb.observe(this, Observer {
                bindUINumber(it)
            })

        return rootView;
    }

    fun bindUI(it: Response?){
        ///val price = Math.round(it!!.data.quote.USD.price * 100.0) / 100.0
        Log.i(TAG,"information"+it!!.data.get(position).cmc_rank)
        Log.i(TAG,"information"+it.data.get(position).name)
        Log.i(TAG,"info pos"+it!!.data.get(position).name)
        coin_title.text = it!!.data.get(position).name
        coin_symbol.text = it.data.get(position).symbol
        coin_price.text = it.data.get(position).quote!!.USD.price.toString() + "$"
        coin_percent_change_1h.text = it.data.get(position).quote!!.USD.percent_change_1h.toString()+"%"
        coin_percent_change_24h.text = it.data.get(position).quote!!.USD.percent_change_24h.toString()+"%"
        coin_percent_change_7d.text = it.data.get(position).quote!!.USD.percent_change_7d.toString()+"%"
        available_supply.text = it.data.get(position).slug
        //val dating = it.data.get(position).last_updated

        last_updated.text = it.data.get(position).last_updated

        Picasso.get()
            .load(if ( (StringBuilder("https://res.cloudinary.com/dxi90ksom/image/upload/")
                    .append(it.data.get(position).symbol.toLowerCase()).append(".png").toString())!= null ) {(StringBuilder("https://res.cloudinary.com/dxi90ksom/image/upload/")
                .append(it.data.get(position).symbol.toLowerCase()).append(".png").toString())}else "https://res.cloudinary.com/dxi90ksom/image/upload/btc.png"  )
            // .error(R.drawable.btcbig)
            .into(image_coin)
    }

    fun bindUINumber(it: Number?){
        ///val price = Math.round(it!!.data.quote.USD.price * 100.0) / 100.0
        Log.i(TAG,"information"+it!!.cmc_rank)
        Log.i(TAG,"information"+it.name)
        Log.i(TAG,"info pos"+it!!.name)
        coin_title.text = it!!.name
        coin_symbol.text = it.symbol
        coin_price.text = it.quote!!.USD.price.toString() + "$"
        coin_percent_change_1h.text = it.quote!!.USD.percent_change_1h.toString()+"%"
        coin_percent_change_24h.text = it.quote!!.USD.percent_change_24h.toString()+"%"
        coin_percent_change_7d.text = it.quote!!.USD.percent_change_7d.toString()+"%"
        available_supply.text = it.slug
        //val dating = it.data.get(position).last_updated

        last_updated.text = it.last_updated

        Picasso.get()
            .load(if ( (StringBuilder("https://res.cloudinary.com/dxi90ksom/image/upload/")
                    .append(it.symbol.toLowerCase()).append(".png").toString())!= null ) {(StringBuilder("https://res.cloudinary.com/dxi90ksom/image/upload/")
                .append(it.symbol.toLowerCase()).append(".png").toString())}else "https://res.cloudinary.com/dxi90ksom/image/upload/btc.png"  )
            // .error(R.drawable.btcbig)
            .into(image_coin)
    }


    private fun getViewModel(rank:Int): SingleCoinViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleCoinViewModel(coinDetailRepository,rank,numberDAO) as T
            }
        })[SingleCoinViewModel::class.java]
    }
}