package com.myapps.wtsacademy2.Activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.myapps.wtsacademy2.ModelClasses.ProductsMD
import com.myapps.wtsacademy2.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ProductDetailsActivity : BaseActivity() {
    private lateinit var productImage: ImageView
    private lateinit var productId: TextView
    private lateinit var productTitle: TextView
    private lateinit var productDescription: TextView
    private lateinit var productStatus: TextView
    private lateinit var productCreatedAt: TextView
    private lateinit var productUpdatedAt: TextView
    private lateinit var product : ProductsMD.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details2)
        initializeViews()
        retriveData()
        setDataToViews()
    }
    private fun initializeViews() {
        productImage = findViewById(R.id.product_image)
        productId = findViewById(R.id.product_id)
        productTitle = findViewById(R.id.product_title)
        productDescription = findViewById(R.id.product_description)
        productStatus = findViewById(R.id.product_status)
        productCreatedAt = findViewById(R.id.product_created_at)
        productUpdatedAt = findViewById(R.id.product_updated_at)
    }
    private fun retriveData(){
        val json = intent.getStringExtra("data")
        product = Gson().fromJson(json, ProductsMD.Data::class.java)
    }
    private fun setDataToViews(){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        productId.text = "ID: ${product._id}"
        productTitle.text = product.title
        productDescription.text = product.description
        productStatus.text = "Status: ${product.status}"
        Picasso.get().load(product.image).into(productImage)

        val createdAtDate = dateFormat.format(parseDateString(product.createdAt))
        productCreatedAt.text = "Created At: $createdAtDate"

        // Format updatedAt
        val updatedAtDate = dateFormat.format(parseDateString(product.updatedAt))
        productUpdatedAt.text = "Updated At: $updatedAtDate"
    }
    private fun parseDateString(dateString: String?): Date {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        try {
            return inputFormat.parse(dateString) ?: Date()
        } catch (e: Exception) {
            e.printStackTrace()
            return Date()
        }
    }
}