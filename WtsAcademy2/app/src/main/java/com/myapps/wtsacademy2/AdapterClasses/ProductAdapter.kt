package com.myapps.wtsacademy2.AdapterClasses

//import android.R
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.myapps.wtsacademy2.Activities.ProductDetailsActivity
import com.myapps.wtsacademy2.Interfaces.ProductActionListener
import com.myapps.wtsacademy2.ModelClasses.ProductsMD
import com.myapps.wtsacademy2.R
import com.squareup.picasso.Picasso


class ProductAdapter(private val productList :ArrayList<ProductsMD.Data>,private val listener: ProductActionListener):RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    private var filteredList: List<ProductsMD.Data> = productList
    inner class MyViewHolder(view:View):ViewHolder(view) {
        private val title: MaterialTextView = view.findViewById(R.id.tvProductTitle)
        private val description: MaterialTextView = view.findViewById(R.id.tvProductDescription)
        private val imageView :ImageView = view.findViewById(R.id.ivPImage)
        private val editButton :ImageButton = view.findViewById(R.id.editProductBtn)
        private val deleteButton :ImageButton = view.findViewById(R.id.deleteProductBtn)
        fun bind(item: ProductsMD.Data){
            title.text = item.title
            description.text = item.description
            if (!item.image.isNullOrEmpty()) {
                Picasso.get().load(item.image).placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground).into(imageView)
            } else {
                Picasso.get().load(R.drawable.ic_launcher_foreground).into(imageView)
            }
            editButton.setOnClickListener { listener.onEditProduct(item) }
            deleteButton.setOnClickListener { listener.onDeleteProduct(item) }
            itemView.setOnClickListener {
                val intent = Intent(it.context,ProductDetailsActivity::class.java)
                val json = Gson().toJson(item)
                intent.putExtra("data",json)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_card,parent,false))
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }
    fun filter(text: String) {
        filteredList = productList.filter {
            it.title?.startsWith(text, true) ?: false
        } as ArrayList<ProductsMD.Data>
        notifyDataSetChanged()
    }
}