package com.myapps.newsapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.myapps.newsapp.adapters.ViewPagerAdapter
import com.myapps.newsapp.databinding.ActivityMainBinding
import com.myapps.newsapp.dataclass.Article


var newsArrayList = ArrayList<Article>()

class MainActivity : AppCompatActivity() {

    val tabArray = arrayOf("technology","science","health","sports","business")

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager2
        val viewAdapter = ViewPagerAdapter(supportFragmentManager,lifecycle,tabArray)
        tabAndViewpagerSettings(viewPager,viewAdapter,tabLayout)
    }

    fun tabAndViewpagerSettings(viewPager:ViewPager2, viewAdapter:ViewPagerAdapter, tabLayout:TabLayout){
        viewPager.adapter = viewAdapter

        TabLayoutMediator(tabLayout,viewPager){
                tab,position-> tab.text =tabArray[position]
        }.attach()
    }
}