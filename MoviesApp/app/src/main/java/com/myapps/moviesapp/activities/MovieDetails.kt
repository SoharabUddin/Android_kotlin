package com.myapps.moviesapp.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myapps.moviesapp.R
import com.myapps.moviesapp.Utils.Constant
import com.myapps.moviesapp.databinding.ActivityMovieDetailsBinding

class MovieDetails : AppCompatActivity() {
    lateinit var binding: ActivityMovieDetailsBinding
    lateinit var imageView: ImageView
    lateinit var titleText : TextView
    lateinit var releaseDate : TextView
    lateinit var category : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = binding.movieDetailsImageView
        titleText = binding.movieDetailsTitleText
        releaseDate = binding.movieDetailsReleaseDate
        category = binding.movieDetailsCategory
        setValues()
    }

    fun setValues(){
        Constant.setImage(imageView)
        val item = Constant.clicekedItem
        titleText.text = item.titleText.text
        releaseDate.text = "${item.releaseDate.day} - ${item.releaseDate.month} - ${item.releaseDate.year}"
        if (item.titleType.categories!=null){
            category.text = item.titleType.categories.getOrNull(0)?.value
        }

    }
}