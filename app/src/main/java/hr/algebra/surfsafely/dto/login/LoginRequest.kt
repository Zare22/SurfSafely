package hr.algebra.surfsafely.dto.login

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(val username: String, val password: String)
