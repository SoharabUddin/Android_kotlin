package com.myapps.wtsacademy2.Activities

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.myapps.wtsacademy2.ApiWorks.ApiService
import com.myapps.wtsacademy2.ApiWorks.ServiceBuilder
import com.myapps.wtsacademy2.ModelClasses.ProductsMD
import com.myapps.wtsacademy2.ModelClasses.SignInMD
import com.myapps.wtsacademy2.Utils.FileUploadHelper
import com.myapps.wtsacademy2.AdapterClasses.ProductAdapter
import com.myapps.wtsacademy2.Interfaces.ProductActionListener
import com.myapps.wtsacademy2.ModelClasses.CreateProductResponse
import com.myapps.wtsacademy2.R
import com.myapps.wtsacademy2.Utils.Constant
import com.myapps.wtsacademy2.Utils.FileUploadHelper.createMultipartFile
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProductListActivity : BaseActivity(), ProductActionListener {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarCP: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: ArrayList<ProductsMD.Data>
    private lateinit var signInMD: SignInMD
    private lateinit var addProductButton: ImageButton
    private lateinit var searchEDT: EditText
    private lateinit var token: String
    private lateinit var retrofit: ApiService
    private lateinit var file: File
    private lateinit var adapter : ProductAdapter
    private lateinit var dialogImageView :ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_product_list, findViewById(R.id.content_frame))
        initializeViews()
        fetchTokenAndCallApi()
        addProductButton.setOnClickListener {
            showAddProductDialog()
        }
        searchEDT.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {            }

            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }
        })
    }

    private fun initializeViews() {
        progressBar = findViewById(R.id.progressBarProduct)
        recyclerView = findViewById(R.id.recyclerViewProduct)
        addProductButton = findViewById(R.id.addProductButton)
        searchEDT = findViewById(R.id.searchEDT)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productList = arrayListOf()
    }

    private fun fetchTokenAndCallApi() {
        token = loadTokenFromPreference()
        if (token.isNotEmpty()) {
            updateHeader()
            displayProductList(token)
        } else {
            showToast("Failed to load token")
        }
    }

    private fun loadTokenFromPreference(): String {
        val sharedPreferences = getSharedPreferences("signInResponse", MODE_PRIVATE)
        val json = sharedPreferences.getString("user_data", "")
        return if (!json.isNullOrEmpty()) {
            try {
                signInMD = Gson().fromJson(json, SignInMD::class.java)
                signInMD.token ?: ""
            } catch (e: Exception) {
                showToast("Error parsing JSON: ${e.message}")
                ""
            }
        } else {
            showToast("No user data found in SharedPreferences")
            ""
        }
    }
    private fun updateHeader(){
        val name = "${signInMD.data?.first_name} ${signInMD.data?.last_name}"?:"Name"
        val email = signInMD.data?.email?:"email@mail.com"
        val profile_url = signInMD.data?.profile_pic?:""
        updateHeader(name,email,profile_url)
    }

    private fun displayProductList(token: String) {
        retrofit = ServiceBuilder.createService(ApiService::class.java)
        retrofit.getProductList(token).enqueue(object : Callback<ProductsMD> {
            override fun onResponse(call: Call<ProductsMD>, response: Response<ProductsMD>) {
                handleProductListResponse(response)
            }

            override fun onFailure(call: Call<ProductsMD>, t: Throwable) {
                showToast("API call failed: ${t.message}")
            }
        })
    }

    private fun handleProductListResponse(response: Response<ProductsMD>) {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                productList.clear()
                productList.addAll(body.data)
                adapter = ProductAdapter(productList, this)
                recyclerView.adapter = adapter
                recyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            } else {
                showToast("Response body is null")
            }
        } else {
            showToast("API call not successful: ${response.errorBody()?.string()}")
        }
    }

    private fun showAddProductDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.create_product, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        progressBarCP = dialogView.findViewById(R.id.progressBarCreateProduct)
        dialogImageView = dialogView.findViewById(R.id.ivProductImage)

        val uploadImage: Button = dialogView.findViewById(R.id.btnUploadImage)
        uploadImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        val createProductButton: Button = dialogView.findViewById(R.id.btnCreateProduct)
        createProductButton.setOnClickListener {
            handleCreateProductButtonClick(dialogView, dialog)
        }
        dialog.show()
    }

    private fun handleCreateProductButtonClick(dialogView: View, dialog: AlertDialog) {
        progressBarCP.visibility = View.VISIBLE
        val titleTxt = dialogView.findViewById<TextInputEditText>(R.id.etTitle).text.toString()
        val descriptionTxt = dialogView.findViewById<TextInputEditText>(R.id.etDescription).text.toString()
        createProduct(titleTxt, descriptionTxt, dialog)
    }

    private fun createProduct(title: String, description: String, dialog: AlertDialog) {
        val titlePart = FileUploadHelper.createPartFromString(title)
        val descriptionPart = FileUploadHelper.createPartFromString(description)
        val profilePicPart = file.createMultipartFile("image")

        retrofit.createProduct(token, titlePart, descriptionPart, profilePicPart).enqueue(object : Callback<CreateProductResponse> {
            override fun onResponse(call: Call<CreateProductResponse>, response: Response<CreateProductResponse>) {
                handleCreateProductResponse(response, dialog)
            }

            override fun onFailure(call: Call<CreateProductResponse>, t: Throwable) {
                showToast("API call failed: ${t.message}")
                progressBarCP.visibility = View.GONE
            }
        })
    }

    private fun handleCreateProductResponse(response: Response<CreateProductResponse>, dialog: AlertDialog) {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                showToast("Product created successfully")
                progressBarCP.visibility = View.GONE
                dialog.dismiss()
                displayProductList(token)
            } else {
                showToast("Response body is null")
                progressBarCP.visibility = View.GONE
            }
        } else {
            showToast("API call not successful: ${response.errorBody()?.string()}")
            progressBarCP.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        handleMediaPickResult(uri)
    }

    private fun handleMediaPickResult(uri: Uri?) {
        if (uri != null) {
            showToast("Image selected: $uri")
            file = FileUploadHelper.copyUriToInternalPath(this, uri) ?: return
            displaySelectedFile(file)
            Log.d("File Extension", "${file.extension}")
        } else {
            showToast("Image not selected")
        }
    }

    override fun onEditProduct(product: ProductsMD.Data) {
        showEditProductDialog(product)
    }

    private fun showEditProductDialog(product: ProductsMD.Data) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.create_product, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val titleEditText: TextInputEditText = dialogView.findViewById(R.id.etTitle)
        val descriptionEditText: TextInputEditText = dialogView.findViewById(R.id.etDescription)
        dialogImageView = dialogView.findViewById(R.id.ivProductImage)
        val heading : TextView = dialogView.findViewById(R.id.headingTV)
        val uploadImage : Button = dialogView.findViewById(R.id.btnUploadImage)
        val updateProductButton : Button = dialogView.findViewById(R.id.btnCreateProduct)
        progressBar  = dialogView.findViewById(R.id.progressBarCreateProduct)
        uploadImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        titleEditText.setText(product.title)
        descriptionEditText.setText(product.description)
        heading.text = "Update Product"
        updateProductButton.text ="Update Product"
        Picasso.get().load(product.image).into(dialogImageView)


        updateProductButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val newTitle = titleEditText.text.toString()
            val newDescription = descriptionEditText.text.toString()
            updateProduct(product._id!!, newTitle, newDescription, dialog)
        }

        dialog.show()
    }

    private fun updateProduct(productId: String, title: String, description: String, dialog: AlertDialog) {
        if (!::file.isInitialized) {
            showToast("Please select an image")
            progressBar.visibility =View.GONE
            return
        }

        val titlePart = FileUploadHelper.createPartFromString(title)
        val idPart = FileUploadHelper.createPartFromString(productId)
        val descriptionPart = FileUploadHelper.createPartFromString(description)
        val profilePicPart = file.createMultipartFile("image")

        if (profilePicPart != null) {
            retrofit.updateProduct(token, idPart, titlePart, descriptionPart, profilePicPart).enqueue(object : Callback<CreateProductResponse> {
                override fun onResponse(call: Call<CreateProductResponse>, response: Response<CreateProductResponse>) {
                    if (response.isSuccessful) {
                        showToast("Product updated successfully")
                        dialog.dismiss()
                        displayProductList(token)
                    } else {
                        showToast("API call not successful: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<CreateProductResponse>, t: Throwable) {
                    showToast("API call failed: ${t.message}")
                }
            })
        }
    }
    override fun onDeleteProduct(product: ProductsMD.Data) {
        showDeleteProductDialog(product)
    }

    private fun showDeleteProductDialog(product: ProductsMD.Data) {
        AlertDialog.Builder(this)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteProduct(product._id!!)
                dialog.dismiss()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteProduct(productId: String) {
        val idPart = FileUploadHelper.createPartFromString(productId)
        retrofit.deleteProduct(token, idPart).enqueue(object : Callback<CreateProductResponse> {
            override fun onResponse(call: Call<CreateProductResponse>, response: Response<CreateProductResponse>) {
                if (response.isSuccessful) {
                    showToast("Product deleted successfully")
                    displayProductList(token)
                } else {
                    showToast("API call not successful: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<CreateProductResponse>, t: Throwable) {
                showToast("API call failed: ${t.message}")
            }
        })
    }
    private fun displaySelectedFile(file: File) {
        dialogImageView.visibility = View.VISIBLE
        Picasso.get().load(file).into(dialogImageView)
    }
}
