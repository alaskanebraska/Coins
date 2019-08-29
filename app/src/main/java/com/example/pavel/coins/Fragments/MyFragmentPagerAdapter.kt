package com.example.pavel.coins.Fragments

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.pavel.coins.Fragments.FragOne
import com.example.pavel.coins.Fragments.FragTwo

class MyFragmentPagerAdapter(context:Context, fm: FragmentManager?):
    FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment {
        if(p0==0){
            return FragOne()
        }else
            return FragTwo()
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0-> return "Local"
            1-> return "Global"
            else-> return null
        }
    }


}