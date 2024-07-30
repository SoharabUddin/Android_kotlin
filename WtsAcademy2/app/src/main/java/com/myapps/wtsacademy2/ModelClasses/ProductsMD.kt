package com.myapps.wtsacademy2.ModelClasses

data class ProductsMD(
    val currentPage: Int? = 0,
    val `data`: ArrayList<Data> = arrayListOf(),
    val message: String? = "",
    val perPage: Int? = 0,
    val status: Int? = 0,
    val totalPages: Int? = 0,
    val totalRecords: Int? = 0
) {
    data class Data(
        val _id: String? = "",
        val createdAt: String? = "",
        val description: String? = "",
        val image: String? = "",
        val isDeleted: Boolean? = false,
        val status: String? = "",
        val title: String? = "",
        val updatedAt: String? = "",
        val user_id: String? = ""
    )
}