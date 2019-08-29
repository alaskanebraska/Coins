package com.example.pavel.coins.Adapter

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pavel.coins.Model.Number
import com.example.pavel.coins.R
import com.example.pavel.coins.Repository.NetWorkState
import com.example.pavel.coins.SingleCoin.SingleCoinActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class MyPAgedListAdapter(val context: Context) : PagedListAdapter<Number,RecyclerView.ViewHolder>(DataDiffCallbcak()) {

    val COIN_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var ranking: Int = 0
    private lateinit var view2: View

    private var networkState: NetWorkState? = null
    class DataDiffCallbcak: DiffUtil.ItemCallback<Number>() {
        override fun areItemsTheSame(p0: Number, p1: Number): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Number, p1: Number): Boolean {
           return p0 == p1
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == COIN_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.coin, parent, false)
            view2 = view
            return MyItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == COIN_VIEW_TYPE) {
            (holder as MyItemViewHolder).bind(getItem(position),context)
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetWorkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            COIN_VIEW_TYPE
        }
    }

    fun getRank(): Int{
        val viewHolder: MyItemViewHolder =
            MyItemViewHolder(view2)
        ranking = viewHolder.ranking2
        return ranking
    }



    class MyItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        var ranking2: Int = 0

        fun bind(data: Number?,context: Context) {

            val price = Math.round(data?.quote!!.USD.price * 100.0) / 100.0
            itemView.symbol.text = data?.symbol
            itemView.name.text = data?.name
            itemView.money.text = price.toString()+"$"
            itemView.hours.text = data?.quote!!.USD.percent_change_1h.toString()+"%"
            itemView.rank.text = data?.cmc_rank.toString()
            ranking2 = data?.cmc_rank
            itemView.hours.setTextColor(if (data.quote!!.USD.percent_change_1h.toString()!!.contains("-")) Color.parseColor("#FF0000")
            else Color.parseColor("#32CD32")
            )

            Picasso.get()
                .load(if ( (StringBuilder("https://res.cloudinary.com/dxi90ksom/image/upload/")
                        .append(data.symbol.toLowerCase()).append(".png").toString()).length != 0 ) (StringBuilder("https://res.cloudinary.com/dxi90ksom/image/upload/")
                    .append(data.symbol.toLowerCase()).append(".png").toString())else "https://res.cloudinary.com/dxi90ksom/image/upload/btc.png"  )
                // .error(R.drawable.btc)
                .into(itemView.imageView)
            val TAG: String = "myLog"
           // Log.i(TAG,"picture"+(StringBuilder("https://res.cloudinary.com/dxi90ksom/image/upload/")
           //     .append(data.symbol.toLowerCase()).append(".png").toString()))

            itemView.setOnClickListener{
                val intent = Intent(context, SingleCoinActivity::class.java)
                intent.putExtra("rank", data.cmc_rank)
                context.startActivity(intent)
            }

        }

    }



    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetWorkState?) {
            if (networkState != null && networkState == NetWorkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE;
            }
            else  {
                itemView.progress_bar_item.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetWorkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            }
            else {
                itemView.error_msg_item.visibility = View.GONE;
            }
        }
    }


    fun setNetworkState(newNetworkState: NetWorkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }

    }

}