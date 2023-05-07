package hr.algebra.surfsafely.dto.password

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChangePasswordRequest(val oldPassword: String, val newPassword: String)
