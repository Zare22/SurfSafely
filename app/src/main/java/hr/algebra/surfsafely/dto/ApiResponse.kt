package hr.algebra.surfsafely.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T, E>(
    val data: T?,
    val error: E?
)
