package com.myapps.moviesapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.myapps.moviesapp.fragments.MovieListFragment
import com.myapps.moviesapp.fragments.SearchMovieFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val tabArray: Array<String>):
    FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return tabArray.size
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return MovieListFragment()

        }
        return SearchMovieFragment()
    }

}