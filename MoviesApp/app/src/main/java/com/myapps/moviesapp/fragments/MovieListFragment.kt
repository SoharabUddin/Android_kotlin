package com.myapps.moviesapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.myapps.moviesapp.R
import com.myapps.moviesapp.Utils.Constant.arraylist
import com.myapps.moviesapp.Utils.OnClick
import com.myapps.moviesapp.activities.MovieDetails
import com.myapps.moviesapp.adapters.MovieAdapter
import com.myapps.moviesapp.apiWorks.ApiService
import com.myapps.moviesapp.apiWorks.ServiceBuilder
import com.myapps.moviesapp.dataClass.MoviesData
import com.myapps.moviesapp.dataClass.Results
import com.myapps.moviesapp.databinding.FragmentMovieListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListFragment : Fragment(),OnClick {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MovieAdapter
    lateinit var retrofit: ApiService

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        arraylist = arrayListOf()
        adapter = MovieAdapter(arraylist,this@MovieListFragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          retrofit = ServiceBuilder.createService(ApiService::class.java)
        retrofit.fetchData().enqueue(object: Callback<MoviesData> {
            override fun onResponse(call: Call<MoviesData>, response: Response<MoviesData>) {
                if (response.isSuccessful){
                    val temp = response.body()?.results
                    if (temp != null) {
                        arraylist.addAll(temp)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }

                    adapter = MovieAdapter(arraylist,this@MovieListFragment)
                    recyclerView.adapter = adapter
                    binding.progressBar.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<MoviesData>, t: Throwable) {
                Toast.makeText(requireContext(),"Error${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick() {
        val intent = Intent(requireContext(),MovieDetails::class.java)
        startActivity(intent)
    }
}