package hr.algebra.surfsafely.dto.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AvatarDto(val id: Long, val photo: String, val price: Long)