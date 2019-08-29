package com.example.pavel.coins.Model

import android.arch.persistence.room.*
import android.support.annotation.NonNull
import android.support.annotation.Nullable

@Entity(tableName = "NumberClass")
data class Number(@PrimaryKey var id:Int, var name:String, var symbol:String, var slug:String,
             var num_market_pairs: Int, var date_added:String, @Ignore var tags:List<String>,
             var max_supply: Float, var circulating_supply: Double, var total_supply:Double,
             var cmc_rank:Int, @ColumnInfo(name="lasted_updated") var last_updated: String,
             @Embedded var quote:Quote?, var favor: Int = 1000)  {

  constructor(): this(0, "", "","",0,"", arrayListOf(),0.0F,
      0.0,0.0,0,"",null,0)

}