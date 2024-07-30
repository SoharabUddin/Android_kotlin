package com.myapps.moviesapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.myapps.moviesapp.adapters.MovieAdapter
import com.myapps.moviesapp.apiWorks.ApiService
import com.myapps.moviesapp.apiWorks.ServiceBuilder
import com.myapps.moviesapp.dataClass.MoviesData
import com.myapps.moviesapp.dataClass.Results
import com.myapps.moviesapp.databinding.ActivityMainBinding
import com.myapps.moviesapp.fragments.MovieListFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val tabsArray = arrayOf("Movies", "Search")

        val tabLayout: TabLayout = binding.tabLayout
        val viewpager : ViewPager2 = binding.viewPager2

        var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager,lifecycle,tabsArray)
        viewpager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = tabsArray[position]
        }.attach()
    }
}