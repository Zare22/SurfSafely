package hr.algebra.surfsafely.dto.quiz

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnswerDto(
    val id: Long?,
    val text: String,
    val isCorrect: Boolean,
    val questionId: Long?
)
