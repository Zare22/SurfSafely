package hr.algebra.surfsafely.dto.quiz

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SolveAttemptResultDto(
    val correctnessPercentage: Double,
    val quizId: Long
)
