package com.myapps.wtsacademy2.ApiWorks

import com.myapps.wtsacademy2.ModelClasses.ProductsMD
import com.myapps.wtsacademy2.ModelClasses.SignInMD
import com.myapps.wtsacademy2.ModelClasses.SignUpResponse
import com.myapps.wtsacademy2.ModelClasses.CreateProductResponse
import com.myapps.wtsacademy2.ModelClasses.SignInRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("user/signup")
    fun signup(
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part profile_pic: MultipartBody.Part
    ):Call<SignUpResponse>
    @POST("user/signin")
    fun signin(@Body signinRequest: SignInRequest) :Call<SignInMD>

    @POST("product/list")
    fun getProductList(@Header("x-access-token") token :String): Call<ProductsMD>

    @Multipart
    @POST("product/create")
    fun createProduct(
        @Header("x-access-token") token :String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<CreateProductResponse>

    @Multipart
    @POST("product/update")
    fun updateProduct(
        @Header("x-access-token") token :String,
        @Part("id") objId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<CreateProductResponse>

    @Multipart
    @POST("product/remove")
    fun deleteProduct(
        @Header("x-access-token") token :String,
        @Part("id") objId: RequestBody
    ) : Call<CreateProductResponse>
}