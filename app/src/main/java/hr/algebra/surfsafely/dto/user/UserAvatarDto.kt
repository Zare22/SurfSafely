package hr.algebra.surfsafely.dto.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserAvatarDto(val id: Long, val userId: Long, val avatarId: Long, val isProfilePicture: Boolean)