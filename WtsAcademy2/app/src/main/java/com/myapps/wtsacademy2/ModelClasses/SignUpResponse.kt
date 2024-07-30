package com.myapps.wtsacademy2.ModelClasses

data class SignUpResponse(
    val `data`: Data? = Data(),
    val message: String? = "",
    val status: Int? = 0,
    val token: String? = ""
) {
    data class Data(
        val _id: String? = "",
        val createdAt: String? = "",
        val deviceToken: String? = "",
        val deviceType: String? = "",
        val email: String? = "",
        val first_name: String? = "",
        val isActive: Boolean? = false,
        val isDeleted: Boolean? = false,
        val last_name: String? = "",
        val password: String? = "",
        val profile_pic: String? = "",
        val register_type: String? = "",
        val role: String? = "",
        val updatedAt: String? = ""
    )
}