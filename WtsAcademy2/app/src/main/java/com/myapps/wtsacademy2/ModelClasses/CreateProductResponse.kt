package com.myapps.wtsacademy2.ModelClasses

data class CreateProductResponse(
    val `data`: Data? = Data(),
    val message: String? = "",
    val status: Int? = 0
) {
    data class Data(
        val _id: String? = "",
        val createdAt: String? = "",
        val description: String? = "",
        val image: String? = "",
        val isDeleted: Boolean? = false,
        val status: String? = "",
        val title: String? = "",
        val updatedAt: String? = ""
    )
}