package com.myapps.wtsacademy2.ModelClasses

data class SignInMD(
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
        val last_name: String? = "",
        val profile_pic: String? = "",
        val register_type: String? = "",
        val role: String? = "",
        val role_data: RoleData? = RoleData(),
        val updatedAt: String? = ""
    ) {
        data class RoleData(
            val _id: String? = "",
            val desc: String? = "",
            val role: String? = "",
            val roleDisplayName: String? = ""
        )
    }
}