package hr.algebra.surfsafely.dto.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserImageDto(val id: Long, val base64: String, val isProfilePic: Boolean, val price: Double) {
}