package com.myapps.imdb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TitleResultAdapter
    private lateinit var findButton: Button
    private lateinit var searchEDT: AutoCompleteTextView
    private lateinit var progressBar: ProgressBar
    private lateinit var previousSearches: ArrayList<String>
    private lateinit var stringAdapter: ArrayAdapter<String>
    private lateinit var titleResultsList: ArrayList<FindResponse.TitleResults.Result>
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        setContentView(R.layout.activity_main)
        initialize()

        findButton.setOnClickListener {
            val query = searchEDT.text.toString()
            Log.d("MainActivity", "Find button clicked, query: $query")

            if (query.isNotEmpty()) {
                if (!previousSearches.contains(query)) {
                    Log.d("MainActivity", "Query not in previous searches, adding to history and fetching from API")
                    addQueryToSearchHistory(query)
                    getDataFromAPI(query)
                } else {
                    Log.d("MainActivity", "Query found in previous searches, fetching from database")
                    getResultFromDB(query)
                }
                searchEDT.text?.clear()
            }
        }
    }

    private fun initialize() {
        Log.d("MainActivity", "initialize called")
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        findButton = findViewById(R.id.findButton)
        progressBar = findViewById(R.id.progressBar)
        searchEDT = findViewById(R.id.searchEDT)
        titleResultsList = arrayListOf()
        previousSearches = arrayListOf("Soharab", "Soumen", "Najbul")
        stringAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, previousSearches)
        searchEDT.setAdapter(stringAdapter)
        dbHelper = DatabaseHelper(applicationContext)
        val allTitles = dbHelper.getAllTitleNameTexts()
        previousSearches.addAll(allTitles)
        Log.d("MainActivity", "Initialization complete")
    }

    private fun addQueryToSearchHistory(query: String) {
        Log.d("MainActivity", "addQueryToSearchHistory called with query: $query")
        stringAdapter.add(query)
        previousSearches.add(query)
        stringAdapter.notifyDataSetChanged()
    }

    private fun getDataFromAPI(query: String) {
        Log.d("MainActivity", "getDataFromAPI called with query: $query")
        progressBar.visibility = View.VISIBLE
        val retrofit = ServiceBuilder.createService(ApiService::class.java)
        retrofit.find(query = query).enqueue(object : Callback<FindResponse> {
            override fun onResponse(call: Call<FindResponse>, response: Response<FindResponse>) {
                Log.d("MainActivity", "API response received")
                if (response.isSuccessful) {
                    val result = response.body()?.titleResults?.results
                    if (result != null) {
                        Log.d("MainActivity", "API call successful, results size: ${result.size}")
                        titleResultsList.clear()
                        for (item in result) {
                            titleResultsList.add(item)
                            dbHelper.insertTitleResult(query, item)
                        }
                        showToast("Inserted to DataBase")
                        adapter = TitleResultAdapter(titleResultsList)
                        recyclerView.adapter = adapter
                        showToast("Success: ${result.size}")
                    } else {
                        Log.d("MainActivity", "API call successful but result is null")
                        showToast("Result is null")
                    }
                } else {
                    Log.d("MainActivity", "API call not successful, error: ${response.errorBody()?.string()}")
                    showToast("Not success: ${response.errorBody()?.string()}")
                }
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<FindResponse>, t: Throwable) {
                Log.d("MainActivity", "API call failed, error: ${t.message}")
                showToast("Failed: ${t.message}")
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun getResultFromDB(query: String) {
        Log.d("MainActivity", "getResultFromDB called with query: $query")
        val results = dbHelper.getTitleResultsByQuery(query)
        titleResultsList.clear()
        titleResultsList.addAll(results)
        adapter = TitleResultAdapter(titleResultsList)
        recyclerView.adapter = adapter
        showToast("Results from Database")
    }

    private fun showToast(msg: String) {
        Log.d("MainActivity", "showToast called with message: $msg")
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
