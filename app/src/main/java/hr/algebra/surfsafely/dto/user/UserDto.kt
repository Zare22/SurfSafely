package hr.algebra.surfsafely.dto.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val email: String,
    val roleId: Long = 2
)
