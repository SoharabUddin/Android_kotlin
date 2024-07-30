package com.myapps.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.myapps.newsapp.OnItemClickListner
import com.myapps.newsapp.R
import com.myapps.newsapp.dataclass.Article

class NewsAdapter (private var arrayList: ArrayList<Article>, private var listner: OnItemClickListner): RecyclerView.Adapter<NewsAdapter.MyViewHolder>(){
    class MyViewHolder(view: View): ViewHolder(view) {
        var imageView = view.findViewById<ImageView>(R.id.imageView)
        var title:TextView = view.findViewById(R.id.titleTV)
        var publishDate: TextView = view.findViewById(R.id.publishDateTV)
        var description :TextView = view.findViewById(R.id.descriptionTV)

        fun bindView(item: Article){
            Glide.with(imageView).load(item.urlToImage).into(imageView)
            title.text = item.title
            publishDate.text = item.publishedAt
            description.text = item.description
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_news,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
       return arrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       var currrentItem = arrayList[position]
        holder.bindView(currrentItem)
        holder.itemView.setOnClickListener{
            listner.onItemClick(position,currrentItem)
        }

    }
}