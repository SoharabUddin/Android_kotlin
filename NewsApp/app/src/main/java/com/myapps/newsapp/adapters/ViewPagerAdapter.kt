package com.myapps.newsapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.myapps.newsapp.fragments.NewsFragment

class ViewPagerAdapter(supportFragmentManager: FragmentManager,lifecycle:Lifecycle,var  tabArray: Array<String>) :
    FragmentStateAdapter(supportFragmentManager,lifecycle) {

    override fun getItemCount(): Int = tabArray.size

    override fun createFragment(position: Int): Fragment {
        return NewsFragment(tabArray[position])
    }
}
