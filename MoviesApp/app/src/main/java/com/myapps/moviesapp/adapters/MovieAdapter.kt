package com.myapps.moviesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.inflate
import com.myapps.moviesapp.R
import com.myapps.moviesapp.Utils.OnClick
import com.myapps.moviesapp.dataClass.PrimaryImage
import com.myapps.moviesapp.dataClass.Results
import com.squareup.picasso.Picasso
import com.myapps.moviesapp.Utils.Constant

class MovieAdapter(var arrayList : ArrayList<Results>, var listener:OnClick): RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {
   inner class MyViewHolder(view: View):ViewHolder(view){
        var imageView: ImageView = view.findViewById(R.id.imageView)
        var movieId: TextView = view.findViewById(R.id.movieId)
        var titleText: TextView = view.findViewById(R.id.titleText)
        var releaseYear: TextView = view.findViewById(R.id.releaseYear)
       fun setValues(item: Results){
           try {
               val imageUrl = item.primaryImage.url
               Picasso.get().load(imageUrl).resize(150, 150).into(imageView)
           } catch (e: Exception) {
               // Handle any exceptions that occur while loading the image
               e.printStackTrace()
           }

           movieId.text = item._id
           titleText.text = item.titleText.text
           releaseYear.text = item.releaseYear.year.toString()
       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.card_movie,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
       return arrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var currentItem = arrayList[position]
        holder.setValues(currentItem)
        holder.itemView.setOnClickListener{
            Constant.clicekedItem = currentItem
            listener.onItemClick()
        }
    }
}
