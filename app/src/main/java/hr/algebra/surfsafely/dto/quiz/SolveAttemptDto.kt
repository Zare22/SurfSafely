package hr.algebra.surfsafely.dto.quiz

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SolveAttemptDto(
    val answerIds: List<Long>
)
