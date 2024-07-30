package com.myapps.imdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class TitleResultAdapter(private val titleResults: List<FindResponse.TitleResults.Result>) : RecyclerView.Adapter<TitleResultAdapter.TitleResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_title_result, parent, false)
        return TitleResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitleResultViewHolder, position: Int) {
        val titleResult = titleResults[position]
        holder.bind(titleResult)
    }

    override fun getItemCount() = titleResults.size

    class TitleResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagePoster: ImageView = itemView.findViewById(R.id.imagePoster)
        private val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        private val textRelease: TextView = itemView.findViewById(R.id.textRelease)
        private val textType: TextView = itemView.findViewById(R.id.textType)
        private val textCredits: TextView = itemView.findViewById(R.id.textCredits)

        fun bind(titleResult: FindResponse.TitleResults.Result) {
            textTitle.text = titleResult.titleNameText
            textRelease.text = titleResult.titleReleaseText
            textType.text = titleResult.imageType
            textCredits.text = titleResult.topCredits?.joinToString(", ")
            val photoPath = titleResult.titlePosterImageModel?.url
            if (!photoPath.isNullOrEmpty()){
                Picasso.get().load(photoPath).into(imagePoster)
            }
            else{
                Picasso.get().load(R.drawable.card_background).into(imagePoster)
            }

        }
    }
}
