package com.example.pavel.coins.SingleCoin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.pavel.coins.Fragments.MyFragmentPagerAdapter
import com.example.pavel.coins.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_single_coin.*

class SingleCoinActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_coin)
        setSupportActionBar(toolbar2)
        val actionBar = supportActionBar
        actionBar!!.title = "Coins"

        val myFragmentPagerAdapter: MyFragmentPagerAdapter =
            MyFragmentPagerAdapter(this, supportFragmentManager)
        viewpager.adapter = myFragmentPagerAdapter
        sliding_tabs.setupWithViewPager(viewpager)


    }

}
