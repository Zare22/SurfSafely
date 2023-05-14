package hr.algebra.surfsafely.dto.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FullUserDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val email: String,
    val role: RoleDto
)

@JsonClass(generateAdapter = true)
data class RoleDto(
    val id: Long,
    val name: String
)
