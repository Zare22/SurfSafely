package hr.algebra.surfsafely.dto.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserUpdateInformationDto(val newEmail: String, val newFirstName: String, val newLastName: String)
