package com.myapps.newsapp

import com.myapps.newsapp.dataclass.Article

interface OnItemClickListner {

    fun onItemClick(position:Int, item:Article)

}