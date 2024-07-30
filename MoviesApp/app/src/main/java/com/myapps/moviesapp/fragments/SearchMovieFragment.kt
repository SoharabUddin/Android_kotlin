package com.myapps.moviesapp.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.myapps.moviesapp.R
import com.myapps.moviesapp.Utils.Constant.arraylist
import com.myapps.moviesapp.dataClass.Results
import com.myapps.moviesapp.databinding.FragmentSearchMovieBinding

class SearchMovieFragment : Fragment() {
   private var _binding : FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cardView = layoutInflater.inflate(R.layout.card_movie,null)
        binding.searchET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
//                Toast.makeText(requireContext(),s,Toast.LENGTH_SHORT).show()
                setVeluesInCardView(cardView)
                binding.container.removeAllViews()
                binding.container.addView(cardView)
                filterResults(s.toString())
            }
        })
    }

    private fun setVeluesInCardView(cardView: View) {
        var titleText: String = binding.searchET.text.toString().toLowerCase()
        var item = searchCard(titleText)

        val id : TextView = cardView.findViewById(R.id.movieId)
        val title : TextView = cardView.findViewById(R.id.titleText)
        val releaseYear : TextView = cardView.findViewById(R.id.releaseYear)

        id.text = item._id
        title.text = item.titleText.text
        releaseYear.text = item.releaseYear.year.toString()
    }

    fun searchCard(title:String): Results{
        var result: Results = arraylist[0]
        for (item: Results in arraylist){
            if( item.titleText.text.toLowerCase().startsWith(title)){
                result=  item
            }
        }
        return result
    }
    fun filterResults(title: String) {
        val filteredResults = ArrayList<Results>()
        for (result in arraylist) {
            if (result.titleText.text.startsWith(title, ignoreCase = true)) {
                filteredResults.add(result)
            }
        }
//        Toast.makeText(requireContext(),"${filteredResults.size}",Toast.LENGTH_SHORT).show()
    }
}