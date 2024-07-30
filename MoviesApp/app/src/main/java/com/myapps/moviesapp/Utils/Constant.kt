package com.myapps.moviesapp.Utils

import android.widget.ImageView
import android.widget.LinearLayout
import com.myapps.moviesapp.R
import com.myapps.moviesapp.dataClass.Results
import com.squareup.picasso.Picasso

object Constant {


    var arraylist: ArrayList<Results> = arrayListOf()
    lateinit var clicekedItem : Results

    fun setImage(imageView:ImageView){
        var url =""
        var width: Int
        var height :Int
        if (clicekedItem.primaryImage!=null){
            url = clicekedItem.primaryImage.url
            width = clicekedItem.primaryImage.width
            height = clicekedItem.primaryImage.height
            Picasso.get().load(url).resize(width, height).centerCrop() .into(imageView)
        }
        else{
            imageView.setImageResource(R.drawable.img)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,700)
            imageView.layoutParams = layoutParams
        }

    }
}