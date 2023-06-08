package hr.algebra.surfsafely.dto.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserPointsDto(val id: Long, val userId: Long, val score: Long, val money: Long)