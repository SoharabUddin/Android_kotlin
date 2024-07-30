package com.myapps.newsapp.dataclass

data class NewsData(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)