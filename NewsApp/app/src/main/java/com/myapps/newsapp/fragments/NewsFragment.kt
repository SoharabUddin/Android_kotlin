package com.myapps.newsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapps.newsapp.OnItemClickListner
import com.myapps.newsapp.activities.WebView
import com.myapps.newsapp.activities.newsArrayList
import com.myapps.newsapp.adapters.NewsAdapter
import com.myapps.newsapp.api_works.ApiService
import com.myapps.newsapp.api_works.ServiceBuilder
import com.myapps.newsapp.databinding.FragmentNewsBinding
import com.myapps.newsapp.dataclass.Article
import com.myapps.newsapp.dataclass.NewsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment(private val category:String) : Fragment() , OnItemClickListner {
    private lateinit var binding: FragmentNewsBinding
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNewsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = ServiceBuilder.createService(ApiService::class.java)
        retrofit.fetchData("in",category,"084fb01e67aa4171b506a77fc192d917").enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                if (response.isSuccessful){
                    val article = response.body()?.articles
                    if (article != null) {
                        newsArrayList.clear()
                        newsArrayList.addAll(article)
                        recyclerView.adapter = NewsAdapter(newsArrayList,this@NewsFragment)
                    }
                    Toast.makeText(context,"Total ${newsArrayList.size} news & Category: $category",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {
                Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show()
            }
        })

    }
    override fun onItemClick(position: Int, item:Article) {
        Toast.makeText(context,"Clicked on $position index", Toast.LENGTH_SHORT).show()
        val url = item.url
        val intent  = Intent(requireContext(), WebView::class.java )
        intent.putExtra("url",url)
        startActivity(intent)
    }

}